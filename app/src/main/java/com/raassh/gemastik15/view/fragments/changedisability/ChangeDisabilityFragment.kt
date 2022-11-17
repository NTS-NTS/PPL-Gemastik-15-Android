package com.raassh.gemastik15.view.fragments.changedisability

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentChangeDisabilityBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateDisabilityFromView
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeDisabilityFragment : Fragment() {
    private val viewModel by viewModel<ChangeDisabilityViewModel>()
    private var binding: FragmentChangeDisabilityBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeDisabilityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDisabilities = ChangeDisabilityFragmentArgs.fromBundle(requireArguments()).disabilities.toList()
        setDisability(currentDisabilities)

        binding?.apply {
            btnBack.setOnClickListener { findNavController().navigateUp() }
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnSend.setOnClickListener {
                val disabilities = getCheckedDisability(llDisability)

                viewModel.setDisabilityTypes(disabilities).observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Success -> {
                            showLoading(false)
                            root.showSnackbar(getString(R.string.success_change_disability))
                            findNavController().navigateUp()
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            root.showSnackbar(
                                requireContext().translateErrorMessage(it.message)
                            )
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun setDisability(disabilities: List<String>) {
        binding?.apply {
            for (i in 0 until llDisability.childCount) {
                val child = llDisability.getChildAt(i)
                if (child is CheckBox) {
                    val disability = requireContext()
                        .translateDisabilityFromView(child.text.toString())
                    child.isChecked = disabilities.contains(disability)
                }
            }
        }
    }

    private fun getCheckedDisability(container: ViewGroup?) : List<String> {
        val checkedDisability = mutableListOf<String>()

        if (container != null) {
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i)
                if (child is ViewGroup) {
                    getCheckedDisability(child)
                } else {
                    if (child is CheckBox) {
                        if (child.isChecked) {
                            val disability = requireContext()
                                .translateDisabilityFromView(child.text.toString())
                            checkedDisability.add(disability)
                        }
                    }
                }
            }
        }

        return checkedDisability
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                btnSend.isEnabled = false
                pbLoading.visibility = View.VISIBLE
                llDisability.visibility = View.GONE
            } else {
                btnSend.isEnabled = true
                pbLoading.visibility = View.GONE
                llDisability.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}