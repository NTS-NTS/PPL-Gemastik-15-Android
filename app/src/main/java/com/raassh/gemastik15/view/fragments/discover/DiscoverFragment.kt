package com.raassh.gemastik15.view.fragments.discover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.PlaceAdapter
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import com.raassh.gemastik15.view.dialogs.ChooseDisabilityDialog
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : Fragment() {
    private val viewModel by viewModel<DiscoverViewModel>()
    private val sharedViewModel by viewModel<DashboardViewModel>()
    private var binding: FragmentDiscoverBinding? = null

    private val placesAdapter = PlaceAdapter(RecyclerView.HORIZONTAL).apply {
        onItemClickListener = { place ->
            val action = DiscoverFragmentDirections.actionNavigationDiscoverToPlaceDetailFragment(place)
            findNavController().navigate(action)
        }
    }

    private val favoritePlacesAdapter = PlaceAdapter(RecyclerView.HORIZONTAL).apply {
        onItemClickListener = { place ->
            val action = DiscoverFragmentDirections.actionNavigationDiscoverToPlaceDetailFragment(place)
            findNavController().navigate(action)
        }
    }

    private val recommendedPlaces = PlaceAdapter(RecyclerView.HORIZONTAL).apply {
        onItemClickListener = { place ->
            val action = DiscoverFragmentDirections.actionNavigationDiscoverToPlaceDetailFragment(place)
            findNavController().navigate(action)
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
            root.applyInsetter { type(statusBars = true) { padding() } }

            btnSearch.setOnClickListener {
                val facilities = mutableListOf<String>()
                getCheckedFacilities(glFacilities, facilities)

                if (facilities.isEmpty()) {
                    root.showSnackbar(
                        message = getString(R.string.no_facilities_chosen),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )
                    return@setOnClickListener
                }

                val action = DiscoverFragmentDirections
                    .actionNavigationDiscoverToSearchByFacilityFragment(facilities.toTypedArray())
                findNavController().navigate(action)
            }

            btnAll.setOnClickListener {
                val facilities = mutableListOf<String>()
                getCheckedFacilities(glFacilities, facilities)

                findNavController().navigate(
                    DiscoverFragmentDirections.actionNavigationDiscoverToSearchFacilityOptionFragment(
                        facilities.toTypedArray()
                    )
                )
            }

            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val action =
                    DiscoverFragmentDirections.actionNavigationDiscoverToSearchResultFragment()
                action.query = etSearch.text.toString()
                findNavController().navigate(action)
            }

            etSearch.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    ivBackdrop.visibility = View.VISIBLE
                } else {
                    ivBackdrop.visibility = View.GONE
                }
            }

            rvRecent.apply {
                adapter = placesAdapter
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }

            rvFavorite.apply {
                adapter = favoritePlacesAdapter
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }

            rvRecommended.apply {
                adapter = recommendedPlaces
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }
        }

        viewModel.apply {
            recentPlaces.observe(viewLifecycleOwner) {
                placesAdapter.submitList(it)

                if (it.isEmpty()) {
                    binding?.tvRecentEmpty?.visibility = View.VISIBLE
                } else {
                    binding?.tvRecentEmpty?.visibility = View.GONE
                }
            }

            hasDisabilityTypes.observe(viewLifecycleOwner) {
                if (it is Resource.Success && it.getIfNotHandled() == false) {
                    ChooseDisabilityDialog().show(childFragmentManager, "ChooseDisabilityDialog")
                }
            }
        }

        binding?.apply {
            rvRecommended.visibility = View.GONE
            tvRecommended.visibility = View.GONE
            dividerRecommended.visibility = View.GONE

            rvFavorite.visibility = View.GONE
            tvFavorite.visibility = View.GONE
            dividerFavorite.visibility = View.GONE
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) { token ->
                if (token != null) viewModel.setToken(token)

                getFavoritePlaces()
            }

            location.observe(viewLifecycleOwner) {
                Log.d("DiscoverFragment", "Location: $it")
                viewModel.setLocation(it)

                getRecommendedPlaces()
            }
        }
    }

    private fun getFavoritePlaces() {
        viewModel.getFavoritePlaces().observe(viewLifecycleOwner) { places ->
            binding?.apply {
                rvFavorite.visibility = View.GONE
                tvFavorite.visibility = View.GONE
                dividerFavorite.visibility = View.GONE
            }

            when (places) {
                is Resource.Success -> {
                    if (!places.data.isNullOrEmpty()) {
                        binding?.apply {
                            rvFavorite.visibility = View.VISIBLE
                            tvFavorite.visibility = View.VISIBLE
                            dividerFavorite.visibility = View.VISIBLE

                            val placesData = places.data.map { place ->
                                placeItemToEntity(place)
                            }

                            favoritePlacesAdapter.submitList(placesData)
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.root?.showSnackbar(
                        message = places.message ?: getString(R.string.unknown_error),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )

                    requireActivity().checkAuthError(places.message)
                }
                is Resource.Loading -> {
                }
            }

        }
    }

    private fun getRecommendedPlaces() {
        viewModel.recommendedPlaces().observe(viewLifecycleOwner) { places ->
            binding?.apply {
                rvRecommended.visibility = View.GONE
                tvRecommended.visibility = View.GONE
                dividerRecommended.visibility = View.GONE
            }

            when (places) {
                is Resource.Success -> {
                    if (!places.data.isNullOrEmpty()) {
                        binding?.apply {
                            rvRecommended.visibility = View.VISIBLE
                            tvRecommended.visibility = View.VISIBLE
                            dividerRecommended.visibility = View.VISIBLE

                            val placesData = places.data.map { place ->
                                placeItemToEntity(place)
                            }

                            recommendedPlaces.submitList(placesData)
                        }
                    }
                }
                is Resource.Error -> {
                    binding?.root?.showSnackbar(
                        message = places.message ?: getString(R.string.unknown_error),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )
                }
                is Resource.Loading -> {
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}