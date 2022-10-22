package com.raassh.gemastik15.view.fragments.searchfacilityoption

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentSearchFacilityOptionBinding
import com.raassh.gemastik15.utils.getCheckedFacilities
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.view.fragments.searchbyfacility.SearchByFacilityFragmentArgs
import dev.chrisbanes.insetter.applyInsetter
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

        val facilities = SearchByFacilityFragmentArgs.fromBundle(requireArguments()).facilities.toMutableList()
        val mobilityFacilities = mutableListOf<String>()
        val visualFacilities = mutableListOf<String>()
        val hearingFacilities = mutableListOf<String>()

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            setCheckedFromList(glMobility, facilities, true)
            setCheckedFromList(glVisual, facilities, true)
            setCheckedFromList(glHearing, facilities, true)

            getCheckedFacilities(glMobility, mobilityFacilities)
            getCheckedFacilities(glVisual, visualFacilities)
            getCheckedFacilities(glHearing, hearingFacilities)

            setCheckBoxUpdate(glMobility)
            setCheckBoxUpdate(glVisual)
            setCheckBoxUpdate(glHearing)

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
                val checkedFacilities = getAllCheckedFacilities()

                if (checkedFacilities.isEmpty()) {
                    root.showSnackbar(getString(R.string.no_facilities_chosen))
                    return@setOnClickListener
                }

                val action = SearchFacilityOptionFragmentDirections
                    .actionSearchFacilityOptionFragmentToSearchByFacilityFragment(checkedFacilities)
                findNavController().navigate(action)
            }
        }

        viewModel.apply {

            checkedMobility.value = mobilityFacilities
            checkedVisual.value = visualFacilities
            checkedHearing.value = hearingFacilities

            binding?.apply {
                checkedMobility.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty() && it.size == glMobility.childCount) {
                        setFacilitiesButtonState(btnMobility, glMobility, true, "mobility")
                    } else {
                        setFacilitiesButtonState(btnMobility, glMobility, false, "mobility")
                    }
                }

                checkedVisual.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty() && it.size == glVisual.childCount) {
                        setFacilitiesButtonState(btnVisual, glVisual, true, "visual")
                    } else {
                        setFacilitiesButtonState(btnVisual, glVisual, false, "visual")
                    }
                }

                checkedHearing.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty() && it.size == glHearing.childCount) {
                        setFacilitiesButtonState(btnHearing, glHearing, true, "hearing")
                    } else {
                        setFacilitiesButtonState(btnHearing, glHearing, false, "hearing")
                    }
                }
            }
        }
    }

    private fun setCheckBoxUpdate(container: ViewGroup) {

        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is CheckBox) {
                child.setOnCheckedChangeListener { _, _ ->
                    val checkedFacilities = mutableListOf<String>()

                    binding?.apply {
                        when (container.id) {
                            R.id.gl_mobility -> {
                                getCheckedFacilities(glMobility, checkedFacilities)
                                viewModel.checkedMobility.value = checkedFacilities
                            }
                            R.id.gl_visual -> {
                                getCheckedFacilities(glVisual, checkedFacilities)
                                viewModel.checkedVisual.value = checkedFacilities
                            }
                            R.id.gl_hearing -> {
                                getCheckedFacilities(glHearing, checkedFacilities)
                                viewModel.checkedHearing.value = checkedFacilities
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFacilitiesButtonState(
        button: Button,
        cbGroup: ViewGroup,
        isAll: Boolean,
        stringResName: String
    ) {
        val facilityType = getString(resources.getIdentifier(
            "${stringResName}_facility",
            "string",
            requireContext().packageName
        ))
        val conDescResId = if (isAll) {
            resources.getIdentifier("unselect_all_$stringResName", "string", requireContext().packageName)
        } else {
            resources.getIdentifier("select_all_$stringResName", "string", requireContext().packageName)
        }

        if (isAll) {
            button.setOnClickListener {
                button.announceForAccessibility(getString(R.string.unselect_all_announcement, facilityType))
                setCheckedAll(cbGroup, false)
            }
            button.contentDescription = getString(conDescResId)
            button.text = getString(R.string.unselect_all)
        } else {
            button.setOnClickListener {
                button.announceForAccessibility(getString(R.string.select_all_announcement, facilityType))
                setCheckedAll(cbGroup, true)
            }
            button.contentDescription = getString(conDescResId)
            button.text = getString(R.string.select_all)
        }
    }

    private fun getAllCheckedFacilities(): Array<String> {
        val facilities = mutableListOf<String>()

        binding?.apply {
            getCheckedFacilities(glMobility, facilities)
            getCheckedFacilities(glVisual, facilities)
            getCheckedFacilities(glHearing, facilities)
        }

        return facilities.toTypedArray()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setCheckedFromList(container: ViewGroup, names: MutableList<String>, state: Boolean) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            if (child is CheckBox) {
                if (names.contains(child.text.toString())) {
                    child.isChecked = state
                    names.remove(child.text.toString())
                }
            }
        }
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