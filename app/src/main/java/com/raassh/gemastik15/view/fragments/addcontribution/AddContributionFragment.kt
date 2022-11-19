package com.raassh.gemastik15.view.fragments.addcontribution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.SingleReviewFacilitiesAdapter
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

        val adapter = SingleReviewFacilitiesAdapter()

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
            tvPlaceType.text = requireContext().translatePlaceTypeNameToView(place.kind)
            tvAddress.text = place.address

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnFacilityReviewGood.setOnClickListener {
                addFacilityReview(2)
            }

            btnFacilityReviewBad.setOnClickListener {
                addFacilityReview(1)
            }

            btnFacilityReviewNone.setOnClickListener {
                addFacilityReview(0)
            }

            btnFacilityReviewDontKnow.setOnClickListener {
                viewModel.nextFacility()
            }

            btnSendContribution.isEnabled = false

            btnSendContribution.setOnClickListener {
                trySubmitContribution(token, place.id, etReview.text.toString())
            }

            binding?.rvYourReviewFacilities?.apply {
                this.adapter = adapter
                layoutManager = FlexboxLayoutManager(requireContext()).apply {
                    flexDirection = FlexDirection.ROW
                    justifyContent = JustifyContent.FLEX_START
                }

                if (itemDecorationCount == 0) {
                    addItemDecoration(FlexboxItemDecoration(requireContext()).apply {
                        setDrawable(AppCompatResources.getDrawable(context, R.drawable.divider))
                        setOrientation(FlexboxItemDecoration.BOTH)
                    })
                }
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

            reviewFacilities.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.submitList(it.toList())
                }
                binding?.btnSendContribution?.isEnabled = !it.isNullOrEmpty()
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

    private fun addFacilityReview(rating: Int) {
        viewModel.addFacilityReview(rating)

        if (viewModel.index.value!! < viewModel.facilities.value!!.size) {
            binding?.llFacilityReviewTitle?.performAccessibilityAction(
                AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,
                null
            )
        }
    }

    private fun trySubmitContribution(token: String?, placeId: String, review: String) {
        if (token.isNullOrEmpty()) {
            return
        }

        viewModel.submitReview(token, placeId, review).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Success -> {
                        showSendLoading(false)
                        binding?.root?.showSnackbar(getString(R.string.review_sent))
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        showSendLoading(false)
                        binding?.root?.showSnackbar(
                            requireContext().translateErrorMessage(it.message)
                        )
                    }
                    is Resource.Loading -> {
                        showSendLoading(true)
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

    private fun showSendLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                btnSendContribution.isEnabled = false
                llReviewFull.visibility = View.GONE
                llReviewSending.visibility = View.VISIBLE
            } else {
                btnSendContribution.isEnabled = true
                llReviewFull.visibility = View.VISIBLE
                llReviewSending.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}