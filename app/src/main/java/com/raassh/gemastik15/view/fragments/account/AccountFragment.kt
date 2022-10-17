package com.raassh.gemastik15.view.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentAccountBinding
import com.raassh.gemastik15.utils.showSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {
    private val viewModel by viewModel<AccountViewModel>()
    private var binding: FragmentAccountBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnLogout.setOnClickListener {
                root.showSnackbar(getString(R.string.logging_out))
                viewModel.logout()
            }
        }

        viewModel.apply {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}