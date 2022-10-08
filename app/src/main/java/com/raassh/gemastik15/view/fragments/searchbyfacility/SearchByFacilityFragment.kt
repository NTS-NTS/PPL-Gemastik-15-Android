package com.raassh.gemastik15.view.fragments.searchbyfacility

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.raassh.gemastik15.databinding.FragmentSearchByFacilityBinding
import com.raassh.gemastik15.utils.checkPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchByFacilityFragment : Fragment() {
    private val viewModel by viewModel<SearchByFacilityViewModel>()
    private var binding: FragmentSearchByFacilityBinding? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var map: GoogleMap? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLastLocation()
            }
            else -> {
                // No location access granted.
                Log.d("TAG", "requestPermissionLauncher: Permissions not granted")
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
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

        showResult(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (requireContext().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            requireContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val current = LatLng(location.latitude, location.longitude)
                    map?.addMarker(MarkerOptions().position(current).title("Marker"))
                    map?.moveCamera(CameraUpdateFactory.newLatLng(current))
                    showResult(false)
                } else {
                    Log.d("TAG", "getMyLastLocation: Location not found")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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
        fusedLocationClient = null
        map = null
    }
}