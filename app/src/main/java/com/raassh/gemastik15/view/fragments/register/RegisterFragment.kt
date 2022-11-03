package com.raassh.gemastik15.view.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentRegisterBinding
import com.raassh.gemastik15.utils.*
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private val viewModel by viewModel<RegisterViewModel>()
    private var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnRegister.setOnClickListener {
                tryRegister()
            }

            btnLogin.setOnClickListener {
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun tryRegister() {
        with(binding ?: return) {
            val isEmailValid = ilEmail.validate(getString(R.string.email)) {
                if (it.isValidEmail()) null else getString(R.string.email_invalid)
            }
            val isNameValid = ilName.validate(getString(R.string.name))
            val isUsernameValid = ilUsername.validate(getString(R.string.username)) {
                if (it.contains(" ")) getString(R.string.username_invalid) else null
            }
            val isPasswordValid = ilPassword.validate(getString(R.string.password)) {
                if (it.isValidPassword()) null else getString(R.string.password_invalid)
            }

            if (!(isEmailValid && isNameValid && isPasswordValid && isUsernameValid)) return

            viewModel.register(
                etName.text.toString(),
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
            ).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            btnRegister.text = getString(R.string.registering)
                            btnRegister.isEnabled = false
                        }
                        is Resource.Success -> {
                            val action =
                                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            action.username = response.data?.username ?: ""

                            findNavController().navigate(action)
                        }
                        is Resource.Error -> {
                            btnRegister.text = getString(R.string.register)
                            btnRegister.isEnabled = true

                            root.showSnackbar(
                                requireContext().translateErrorMessage(response.message)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}