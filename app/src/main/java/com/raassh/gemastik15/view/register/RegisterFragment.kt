package com.raassh.gemastik15.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.ErrorResponse
import com.raassh.gemastik15.api.response.UserData
import com.raassh.gemastik15.databinding.FragmentRegisterBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
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
            btnRegister.setOnClickListener {
                viewModel.register(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                ).observe(viewLifecycleOwner) { response ->
                    if (response != null) {
                        when (response) {
                            is Resource.Loading -> {
                                btnRegister.isEnabled = false
                            }
                            is Resource.Success -> {
                                btnRegister.isEnabled = true

                                val action = RegisterFragmentDirections
                                    .actionRegisterFragmentToLoginFragment()
                                action.username = (response.data as UserData).email

                                findNavController().navigate(action)
                            }
                            is Resource.Error -> {
                                btnRegister.isEnabled = true

                                val error = response.data as ErrorResponse?

                                root.showSnackbar(
                                    error?.data ?: getString(R.string.unknown_error)
                                )
                            }
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