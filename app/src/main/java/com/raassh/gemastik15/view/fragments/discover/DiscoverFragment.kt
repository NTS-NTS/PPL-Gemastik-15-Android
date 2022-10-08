package com.raassh.gemastik15.view.fragments.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.databinding.FragmentDiscoverBinding
import com.raassh.gemastik15.utils.getCheckedFacilities
import com.raassh.gemastik15.utils.on
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

        binding?.apply {
            btnSearch.setOnClickListener {
                val facilities = mutableListOf<String>()
                getCheckedFacilities(glFacilities, facilities)

                val action = DiscoverFragmentDirections
                    .actionNavigationDiscoverToSearchByFacilityFragment(facilities.toTypedArray())
                findNavController().navigate(action)
            }

            btnAll.setOnClickListener {
                findNavController().navigate(DiscoverFragmentDirections.actionNavigationDiscoverToSearchFacilityOptionFragment())
            }

            etSearch.on(EditorInfo.IME_ACTION_DONE) {
                val action = DiscoverFragmentDirections.actionNavigationDiscoverToSearchResultFragment()
                action.query = etSearch.text.toString()
                findNavController().navigate(action)
            }
        }

        viewModel.apply {
            //
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}