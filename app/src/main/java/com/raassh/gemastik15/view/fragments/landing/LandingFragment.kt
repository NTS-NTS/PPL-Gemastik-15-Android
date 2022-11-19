package com.raassh.gemastik15.view.fragments.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentLandingBinding
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class LandingFragment : Fragment() {
    private val viewModel by viewModel<LandingViewModel>()
    private var binding: FragmentLandingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                findNavController().navigate(R.id.action_landingFragment_to_registerFragment)
            }
        }

        viewModel.apply {
            //
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}