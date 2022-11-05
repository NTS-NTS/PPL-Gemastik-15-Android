package com.raassh.gemastik15.view.fragments.addcontribution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.databinding.FragmentAddContributionBinding
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Field

class AddContributionFragment : Fragment() {
    private val viewModel by viewModel<AddContributionViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentAddContributionBinding? = null

    private val callback = OnMapReadyCallback { googleMap ->
        setPlaceDetail(place, googleMap)
    }

    private var token: String? = null
    private lateinit var place: PlaceDetailData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContributionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        place = AddContributionFragmentArgs.fromBundle(requireArguments()).place

        val mapFragment = binding?.fragmentMap?.getFragment<SupportMapFragment?>()
        mapFragment?.getMapAsync(callback)
        mapFragment?.view?.apply {
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
            isFocusable = false
        }

        showReviewLoading(true)
        prepareFacilityReviewData()

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            tvPlaceName.text = place.name
            if (place.distance == -1.0) {
                tvPlaceDistance.visibility = View.INVISIBLE
                ivDot.visibility = View.INVISIBLE
            } else {
                tvPlaceDistance.text = getString(R.string.distance, place.distance.rounded(2))
            }
            tvPlaceType.text = requireContext().translateTypeName(place.kind)
            tvAddress.text = place.address

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFacilityReviewDone.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFacilityReviewGood.setOnClickListener {
                trySubmitContribution(token, place.id, 2)
            }

            btnFacilityReviewBad.setOnClickListener {
                trySubmitContribution(token, place.id, 1)
            }

            btnFacilityReviewNone.setOnClickListener {
                trySubmitContribution(token, place.id, 0)
            }

            btnFacilityReviewDontKnow.setOnClickListener {
                viewModel.nextFacility()
            }
        }

        viewModel.apply {
            currentFacility.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding?.apply {
                        tvFacilityName.text = getString(getResId(it.name, R.string::class.java))
                        tvFacilityDescription.text = getString(getResId(it.description, R.string::class.java))
                        imgFacilityIcon.setImageResource(getResId(it.icon, R.drawable::class.java))
                    }
                }
            }

            index.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding?.apply {
                        tvFacilityCounter.text = getString(
                            R.string.facility_counter,
                            it + 1,
                            facilities.value?.size
                        )
                    }
                }
            }

            isDone.observe(viewLifecycleOwner) {
                binding?.apply {
                    if (it) {
                        llFacilityReviewDone.visibility = View.VISIBLE
                        llFacilities.visibility = View.GONE

                        tvFacilityReviewDone.performAccessibilityAction(
                            AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS,
                            null
                        )
                    } else {
                        llFacilityReviewDone.visibility = View.GONE
                        llFacilities.visibility = View.VISIBLE
                    }
                }
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    token = it
                    showReviewLoading(false)
                }
            }
        }
    }

    private fun trySubmitContribution(token: String?, placeId: String, rating: Int) {
        if (token.isNullOrEmpty()) {
            return
        }

        viewModel.submitContribution(token, placeId, rating)
            .observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            showReviewLoading(true)
                        }
                        is Resource.Success -> {
                            showReviewLoading(false)
                            viewModel.nextFacility()

                            if (viewModel.index.value!! < viewModel.facilities.value!!.size) {
                                binding?.llFacilityReviewTitle?.performAccessibilityAction(
                                    AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,
                                    null
                                )
                            }
                        }
                        is Resource.Error -> {
                            showReviewLoading(false)

                            binding?.root?.showSnackbar(
                                requireContext().translateErrorMessage(response.message)
                            )

                            requireContext().checkAuthError(response.message)
                        }
                    }
                }
            }
    }

    private fun showReviewLoading(isLoading: Boolean) {
        binding?.apply {
            btnFacilityReviewBad.isEnabled = !isLoading
            btnFacilityReviewGood.isEnabled = !isLoading
            btnFacilityReviewNone.isEnabled = !isLoading
            btnFacilityReviewDontKnow.isEnabled = !isLoading

            spnFacilityReview.visibility = if (isLoading) View.VISIBLE else View.GONE
            imgFacilityIcon.visibility = if (isLoading) View.GONE else View.VISIBLE
            tvFacilityName.visibility = if (isLoading) View.GONE else View.VISIBLE
            tvFacilityDescription.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun setPlaceDetail(detail: PlaceDetailData, map: GoogleMap) {
        val latLng = LatLng(detail.latitude, detail.longitude)
        map.addMarker(MarkerOptions().position(latLng).title(detail.name))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        map.setContentDescription(null)
    }

    private fun prepareFacilityReviewData() {
        val istream = resources.openRawResource(R.raw.facility_data)
        val facilities: List<Facility> = FacilityDataXmlParser().parse(istream)

        viewModel.facilities.value = facilities
        viewModel.setCurrentFacility()
    }

    private fun getResId(resName: String, c: Class<*>): Int {
        return try {
            val idField: Field = c.getDeclaredField(resName)
            idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}