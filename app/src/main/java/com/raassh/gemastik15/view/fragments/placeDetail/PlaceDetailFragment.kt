package com.raassh.gemastik15.view.fragments.placeDetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
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
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceDetailFragment : Fragment() {
    private val viewModel by viewModel<PlaceDetailViewModel>()
    private var binding: FragmentPlaceDetailBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var map: GoogleMap? = null
    private var currentLocation = LatLng(0.0, 0.0)

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

        val placeId = PlaceDetailFragmentArgs.fromBundle(requireArguments()).placeId
        var placeLat = ""
        var placeLng = ""

        binding?.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnMaps.setOnClickListener {
                val geolocation = Uri.parse("geo:$placeLat,$placeLng?z=17")
                val intent = Intent(Intent.ACTION_VIEW, geolocation)
                startActivity(intent)
            }

            btnAddReview.setOnClickListener {
                val action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToAddContributionFragment(placeId)
                findNavController().navigate(action)
            }
        }

        viewModel.detail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    setPlaceDetail(it.data as PlaceDetailData)
                    placeLat = it.data.latitude.toString()
                    placeLng = it.data.longitude.toString()
                    binding?.btnMaps?.isEnabled = true
                }
                is Resource.Error -> {
                    binding?.btnMaps?.isEnabled = false
                }
                is Resource.Loading -> {
                    binding?.btnMaps?.isEnabled = false
                }
            }
        }

        sharedViewModel.location.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setId(placeId, it.latitude, it.longitude)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setPlaceDetail(detail: PlaceDetailData) {
        binding?.apply {
            tvPlaceName.text = detail.name
            tvAddress.text = detail.address
            tvPlaceDistance.text = getString(R.string.distance, detail.distance.rounded(2))
            tvPlaceType.text = detail.kind

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
}