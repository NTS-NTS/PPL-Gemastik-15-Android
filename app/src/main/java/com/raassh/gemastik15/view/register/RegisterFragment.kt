package com.raassh.gemastik15.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.databinding.FragmentRegisterBinding
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
                )
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