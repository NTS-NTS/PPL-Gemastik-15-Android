package com.raassh.gemastik15.view.fragments.searchbyfacility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.raassh.gemastik15.adapter.OptionTagAdapter
import com.raassh.gemastik15.adapter.PlaceAdapter
import com.raassh.gemastik15.api.response.PlacesItem
import com.raassh.gemastik15.databinding.FragmentSearchByFacilityBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchByFacilityFragment : Fragment() {
    private val viewModel by viewModel<SearchByFacilityViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentSearchByFacilityBinding? = null
    private var map: GoogleMap? = null
    private var currentLocation: LatLng? = null

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
                findNavController().navigate(SearchByFacilityFragmentDirections.actionSearchByFacilityFragmentToSearchFacilityOptionFragment(facilities))
            }

            rvResult.apply {
                adapter = PlaceAdapter().apply {
                    onItemClickListener = { place ->
                        val action = SearchByFacilityFragmentDirections.actionSearchByFacilityFragmentToPlaceDetailFragment(place)
                        findNavController().navigate(action)
                    }
                }

                addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.VERTICAL))
            }

            rvOptions.apply {
                adapter = OptionTagAdapter().apply {
                    submitList(facilities.toList())
                }

                addItemDecoration(LinearSpaceItemDecoration(8, RecyclerView.HORIZONTAL))
            }
        }

        viewModel.places.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)

                        showResult(it.data)
                    }
                    is Resource.Error -> {
                        showLoading(false, error = true)

                        binding?.root?.showSnackbar(
                            it.message ?: getString(R.string.unknown_error)
                        )
                    }
                }
            }
        }

        sharedViewModel.location.observe(viewLifecycleOwner) {
            currentLocation = it
            viewModel.searchPlace(facilities.map { name ->
                requireContext().translateViewtoDBName(name)
            }, currentLocation?.latitude, currentLocation?.longitude)
        }
    }

    private fun showLoading(loading: Boolean, error: Boolean = false) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                rvResult.visibility = View.GONE
                tvNoResult.visibility = View.GONE
                fragmentMap.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE

                if (error) {
                    tvNoResult.visibility = View.VISIBLE
                    rvResult.visibility = View.GONE
                    fragmentMap.visibility = View.GONE
                } else {
                    tvNoResult.visibility = View.GONE
                    rvResult.visibility = View.VISIBLE
                    fragmentMap.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showResult(result: List<PlacesItem>?) {
        binding?.apply {
            if (result.isNullOrEmpty()) {
                tvNoResult.visibility = View.VISIBLE
                rvResult.visibility = View.GONE
                fragmentMap.visibility = View.GONE
                return
            }

            (rvResult.adapter as PlaceAdapter).submitList(result.map {
                placeItemToEntity(it)
            })

            val latLngBounds = LatLngBounds.Builder()

            result.forEach {
                val latLng = LatLng(it.latitude, it.longitude)
                map?.addMarker(MarkerOptions().position(latLng).title(it.name))
                latLngBounds.include(latLng)
            }

            map?.setOnMapLoadedCallback {
                map?.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 20))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        map = null
    }
}