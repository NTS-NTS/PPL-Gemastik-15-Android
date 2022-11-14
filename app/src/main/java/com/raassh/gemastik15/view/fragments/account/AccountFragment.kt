package com.raassh.gemastik15.view.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.FragmentAccountBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {
    private val viewModel by viewModel<AccountViewModel>()
    private var binding: FragmentAccountBinding? = null
    private val sharedViewModel by viewModel<DashboardViewModel>()

    private var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themes = resources.getStringArray(R.array.dark_mode_entries)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, themes)
        viewModel.arrayAdapter.value = arrayAdapter

        binding?.apply {
            root.applyInsetter { type(statusBars = true) { padding() } }

            etDarkMode.apply {
                setOnItemClickListener { parent, _, position, _ ->
                    val theme = parent.getItemAtPosition(position).toString()
                    showSnackbar(getString(R.string.theme_changed_message, theme))
                    viewModel.setTheme(getThemeKey(theme))
                }
            }

            btnLogout.setOnClickListener {
                root.showSnackbar(getString(R.string.logging_out))
                viewModel.logout()
            }

            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
            }

            btnChangePassword.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
            }

            btnModeration.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_moderationFragment)
            }
        }

        viewModel.apply {

            arrayAdapter.observe(viewLifecycleOwner) {
                binding?.etDarkMode?.setAdapter(it)
            }

            getTheme().observe(viewLifecycleOwner) {
                if (it != null) {
                    binding?.etDarkMode?.setText(getThemeString(it), false)

                    when (it) {
                        "MODE_NIGHT_YES" -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                        "MODE_NIGHT_NO" -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        "MODE_NIGHT_FOLLOW_SYSTEM" -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                        "MODE_NIGHT_AUTO_BATTERY" -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                        }
                        else -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }
                    }
                }
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    val jwt = JWT(it)
                    val name = jwt.getClaim("name").asString()
                    val email = jwt.getClaim("email").asString()

                    binding?.tvName?.text = name
                    binding?.tvEmail?.text = email
                    binding?.btnResendVerification?.setOnClickListener {
                        if (email != null) {
                            viewModel.resendVerification(email).observe(viewLifecycleOwner) { response ->
                                when (response) {
                                    is Resource.Success -> {
                                        binding?.btnResendVerification?.isEnabled = true
                                        binding?.root?.showSnackbar(getString(R.string.verification_email_sent))
                                    }
                                    is Resource.Error -> {
                                        binding?.btnResendVerification?.isEnabled = true
                                        binding?.root?.showSnackbar(
                                            requireContext().translateErrorMessage(response.message)
                                        )
                                    }
                                    is Resource.Loading -> {
                                        binding?.btnResendVerification?.isEnabled = false
                                    }
                                }
                            }
                        }
                    }
                }
            }

            getIsModerator().observe(viewLifecycleOwner) {
                if (it == true) {
                    binding?.btnModeration?.visibility = View.VISIBLE
                } else {
                    binding?.btnModeration?.visibility = View.GONE
                }
            }

            getIsVerified().observe(viewLifecycleOwner) {
                if (it == true) {
                    binding?.btnResendVerification?.visibility = View.GONE
                } else {
                    binding?.btnResendVerification?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getThemeKey(theme: String): String {
        return when (theme) {
            getString(R.string.dark_mode_yes) -> getString(R.string.dark_mode_yes_key)
            getString(R.string.dark_mode_no) -> getString(R.string.dark_mode_no_key)
            getString(R.string.dark_mode_battery) -> getString(R.string.dark_mode_battery_key)
            getString(R.string.dark_mode_system) -> getString(R.string.dark_mode_system_key)
            else -> getString(R.string.dark_mode_system_key)
        }
    }

    private fun getThemeString(key: String): String {
        return when (key) {
            getString(R.string.dark_mode_yes_key) -> getString(R.string.dark_mode_yes)
            getString(R.string.dark_mode_no_key) -> getString(R.string.dark_mode_no)
            getString(R.string.dark_mode_battery_key) -> getString(R.string.dark_mode_battery)
            getString(R.string.dark_mode_system_key) -> getString(R.string.dark_mode_system)
            else -> getString(R.string.dark_mode_system)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}