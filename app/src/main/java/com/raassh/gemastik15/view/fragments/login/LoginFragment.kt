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
import com.raassh.gemastik15.utils.validate
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
            etEmail.setText(username)

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
            val isEmailValid = ilEmail.validate(getString(R.string.email))
            val isPasswordValid = ilPassword.validate(getString(R.string.password))

            if (!(isEmailValid && isPasswordValid)) return

            viewModel.login(
                etEmail.text.toString(),
                etPassword.text.toString()
            ).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                            btnLogin.isEnabled = false
                        }
                        is Resource.Success -> {
                            btnLogin.isEnabled = true

                            val token = response.data?.token ?: ""
                            viewModel.setToken(token)
                        }
                        is Resource.Error -> {
                            btnLogin.isEnabled = true

                            root.showSnackbar(
                                response.message ?: getString(R.string.unknown_error)
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