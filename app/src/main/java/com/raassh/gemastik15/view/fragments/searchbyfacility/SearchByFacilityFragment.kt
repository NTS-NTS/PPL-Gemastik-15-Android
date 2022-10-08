package com.raassh.gemastik15.view.fragments.searchbyfacility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.databinding.FragmentSearchByFacilityBinding
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchByFacilityFragment : Fragment() {
    private val viewModel by viewModel<SearchByFacilityViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentSearchByFacilityBinding? = null
    private var map: GoogleMap? = null
    private var currentLocation = LatLng(0.0, 0.0)

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap

        map?.addMarker(MarkerOptions().position(currentLocation).title("My Location"))
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        showResult(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchByFacilityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = binding?.fragmentMap?.getFragment<SupportMapFragment?>()
        mapFragment?.getMapAsync(callback)

        val facilities = SearchByFacilityFragmentArgs.fromBundle(requireArguments()).facilities

        binding?.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnEdit.setOnClickListener {
                findNavController().navigate(SearchByFacilityFragmentDirections.actionSearchByFacilityFragmentToSearchFacilityOptionFragment())
            }

        }

        viewModel.apply {
            //
        }

        sharedViewModel.location.observe(viewLifecycleOwner) {
            if (it != null) {
                currentLocation = LatLng(it.latitude, it.longitude)

                map?.addMarker(MarkerOptions().position(currentLocation).title("My Location"))
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
                showResult(false)
            }
        }

        showResult(true)
    }

    private fun showResult(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                groupSearch.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                groupSearch.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        map = null
    }
}