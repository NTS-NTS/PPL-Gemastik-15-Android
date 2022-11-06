package com.raassh.gemastik15.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentChooseDisabilityDialogBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.checkAuthError
import com.raassh.gemastik15.utils.translateDisabilityFromView
import org.koin.androidx.viewmodel.ext.android.viewModel

// this might need to be refactored
class ChooseDisabilityDialog() : DialogFragment() {
    private var binding: FragmentChooseDisabilityDialogBinding? = null
    private val viewModel by viewModel<ChooseDisabilityDialogViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentChooseDisabilityDialogBinding.inflate(layoutInflater, null, false)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding?.root)
            .setTitle(R.string.set_disability_types)
            .setCancelable(false)
            .setPositiveButton(R.string.save) { _, _ ->
                // intentionally left blank
                // see https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
            }
            .create()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            positiveButton.setOnClickListener {
                val disabilities = getCheckedDisability(binding?.clDisabilities)
                viewModel.setDisabilityTypes(disabilities).observe(this) {
                    when (it) {
                        is Resource.Success -> {
                            viewModel.setHasDisability(true)

                            dialog.dismiss()
                        }
                        is Resource.Error -> {
                            requireContext().checkAuthError(it.message)

                            dialog.dismiss()
                        }
                        is Resource.Loading -> {
                            for (i in 0 until binding?.clDisabilities?.childCount!!) {
                                val child = binding?.clDisabilities?.getChildAt(i)
                                if (child !is ProgressBar) {
                                    child?.visibility = View.INVISIBLE
                                }
                            }
                            binding?.progressBar?.visibility = View.VISIBLE
                            positiveButton.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

        return dialog
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}