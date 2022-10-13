package com.raassh.gemastik15.view.fragments.placedetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.FacilityReviewAdapter
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.databinding.FragmentPlaceDetailBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.getFacilitiesGroup
import com.raassh.gemastik15.utils.rounded
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceDetailFragment : Fragment() {
    private val viewModel by viewModel<PlaceDetailViewModel>()
    private var binding: FragmentPlaceDetailBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = binding?.fragmentMap?.getFragment<SupportMapFragment?>()
        mapFragment?.getMapAsync(callback)

        val place = PlaceDetailFragmentArgs.fromBundle(requireArguments()).place
        showLoading(true)

        binding?.apply {
            tvPlaceName.text = place.name
            tvPlaceType.text = place.type
            tvPlaceDistance.text = getString(R.string.distance, place.distance.rounded(2))

            btnMaps.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${place.latitude},${place.longitude}"))
                intent.setPackage("com.google.android.apps.maps")

                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnAddReview.setOnClickListener {
                val action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToAddContributionFragment(place)
                findNavController().navigate(action)
            }
        }

        viewModel.detail.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        setPlaceDetail(it.data as PlaceDetailData)
                    }
                    is Resource.Error -> {
                        binding?.root?.showSnackbar(
                            it.message ?: getString(R.string.unknown_error)
                        )

                        findNavController().navigateUp()
                    }
                }
            }
        }

        sharedViewModel.location.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getDetail(place, it.latitude, it.longitude)
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                content.visibility = View.GONE
                binding?.btnMaps?.isEnabled = false
            } else {
                pbLoading.visibility = View.GONE
                content.visibility = View.VISIBLE
                binding?.btnMaps?.isEnabled = true
            }
        }
    }

    private fun setPlaceDetail(detail: PlaceDetailData) {
        binding?.apply {

            val facilitiesGroup = requireContext().getFacilitiesGroup(detail.facilities)

            showFacilityReviews(facilitiesGroup[0], rvMobilityFacilities, tvMobilityFacilitiesEmpty)
            showFacilityReviews(facilitiesGroup[1], rvVisualFacilities, tvVisualFacilitiesEmpty)
            showFacilityReviews(facilitiesGroup[2], rvAudioFacilities, tvAudioFacilitiesEmpty)
        }

        val latLngBounds = LatLngBounds.Builder()

        val latLng = LatLng(detail.latitude, detail.longitude)
        map?.addMarker(MarkerOptions().position(latLng).title(detail.name))
        latLngBounds.include(latLng)

        map?.setOnMapLoadedCallback {
            map?.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 100))
        }
    }

    private fun showFacilityReviews(facilities: List<FacilitiesItem>, rvReviews: RecyclerView, tvEmpty: TextView) {
        if (facilities.isNotEmpty()) {
            val adapter = FacilityReviewAdapter()
            rvReviews.adapter = adapter
            adapter.submitList(facilities)
        } else {
            rvReviews.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}