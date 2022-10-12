package com.raassh.gemastik15.view.fragments.placeDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.raassh.gemastik15.databinding.FragmentPlaceDetailBinding
import com.raassh.gemastik15.utils.Resource
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

        val placeId = PlaceDetailFragmentArgs.fromBundle(requireArguments()).placeId

        binding?.apply {
            //
        }

        viewModel.detail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("TAG", "onViewCreated: ${it}")

                }
                is Resource.Error -> {
                    Log.d("TAG", "onViewCreated: ${it}")
                }
                is Resource.Loading -> {
                    Log.d("TAG", "onViewCreated: ${it}")
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
}