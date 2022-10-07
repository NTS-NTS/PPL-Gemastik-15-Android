package com.raassh.gemastik15.view.fragments.searchfacilityoption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.databinding.FragmentSearchFacilityOptionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFacilityOptionFragment : Fragment() {
    private val viewModel by viewModel<SearchFacilityOptionViewModel>()
    private var binding: FragmentSearchFacilityOptionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchFacilityOptionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnMobility.setOnClickListener {
                setCheckedAll(glMobility, true)
            }

            btnVisual.setOnClickListener {
                setCheckedAll(glVisual, true)
            }

            btnHearing.setOnClickListener {
                setCheckedAll(glHearing, true)
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSearch.setOnClickListener {
                val action = SearchFacilityOptionFragmentDirections.actionSearchFacilityOptionFragmentToSearchByFacilityFragment()
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

    private fun setCheckedAll(container: ViewGroup, state: Boolean) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is CheckBox) {
                child.isChecked = state
            }
        }
    }
}