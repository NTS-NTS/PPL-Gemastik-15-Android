package com.raassh.gemastik15.view.fragments.discover

import android.os.Bundle
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
import com.raassh.gemastik15.view.dialogs.ChooseDisabilityDialog
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DiscoverFragment : Fragment() {
    private val viewModel by viewModel<DiscoverViewModel>()
    private var binding: FragmentDiscoverBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placesAdapter = PlaceAdapter().apply {
            onItemClickListener = { place ->
                val action = DiscoverFragmentDirections.actionNavigationDiscoverToPlaceDetailFragment(place)
                findNavController().navigate(action)
            }
        }

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
                addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.VERTICAL))
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
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}