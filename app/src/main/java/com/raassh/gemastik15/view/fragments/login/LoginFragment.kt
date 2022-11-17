package com.raassh.gemastik15.view.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentLoginBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.utils.validate
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val viewModel by viewModel<LoginViewModel>()
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = LoginFragmentArgs.fromBundle(
            requireArguments()
        ).username

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            etEmailOrUsername.setText(username)

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnLogin.setOnClickListener {
                tryLogin()
            }
        }
    }

    private fun tryLogin() {
        with(binding ?: return) {
            val isUsernameValid = ilEmailOrUsername.validate(getString(R.string.email_or_username))
            val isPasswordValid = ilPassword.validate(getString(R.string.password))

            if (!(isUsernameValid && isPasswordValid)) return

            viewModel.login(
                etEmailOrUsername.text.toString(),
                etPassword.text.toString()
            ).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            btnLogin.text = getString(R.string.logging_in)
                            btnLogin.isEnabled = false
                        }
                        is Resource.Success -> {
                            btnLogin.isEnabled = true

                            viewModel.setUserData(response.data)
                        }
                        is Resource.Error -> {
                            btnLogin.text = getString(R.string.login)
                            btnLogin.isEnabled = true

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