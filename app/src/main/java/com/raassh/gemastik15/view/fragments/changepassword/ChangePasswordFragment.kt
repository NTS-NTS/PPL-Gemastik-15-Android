package com.raassh.gemastik15.view.fragments.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentChangePasswordBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : Fragment() {
    private val viewModel by viewModel<ChangePasswordViewModel>()
    private var binding: FragmentChangePasswordBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }
            btnChangePassword.setOnClickListener {
                tryChangePassword()
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            viewModel.setToken(it ?: "")
        }
    }

    private fun tryChangePassword() {
        with(binding ?: return) {
            val isOldPasswordValid = ilOldPassword.validate(getString(R.string.old_password))
            val isNewPasswordValid = ilNewPassword.validate(getString(R.string.new_password)) {
                if (it.isValidPassword()) null else getString(R.string.password_invalid)
            }

            if (!(isOldPasswordValid && isNewPasswordValid))
                return

            viewModel.changePassword(
                etOldPassword.text.toString(),
                etNewPassword.text.toString()
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        root.showSnackbar(getString(R.string.password_changed))
                        btnChangePassword.isEnabled = true
                        btnChangePassword.text = getString(R.string.change_password)

                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        btnChangePassword.isEnabled = true
                        btnChangePassword.text = getString(R.string.change_password)

                        root.showSnackbar(
                            requireContext().translateErrorMessage(it.message)
                        )

                        requireActivity().checkAuthError(it.message)
                    }
                    is Resource.Loading -> {
                        btnChangePassword.isEnabled = false
                        btnChangePassword.text = getString(R.string.changing_password)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}