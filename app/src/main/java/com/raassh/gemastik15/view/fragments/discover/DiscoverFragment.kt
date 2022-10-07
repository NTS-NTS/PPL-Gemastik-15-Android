package com.raassh.gemastik15.view.fragments.discover

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
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
import com.raassh.gemastik15.utils.checkPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : Fragment() {
    private val viewModel by viewModel<DiscoverViewModel>()
    private var binding: FragmentDiscoverBinding? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

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

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (requireContext().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            requireContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("TAG", "getMyLastLocation: ${location.latitude}, ${location.longitude}")
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // TODO: Implement search
            btnSearch.setOnClickListener {
                findNavController().navigate(DiscoverFragmentDirections.actionNavigationDiscoverToSearchResultFragment())
            }
        }

        viewModel.apply {
            //
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
        fusedLocationClient = null
    }
}