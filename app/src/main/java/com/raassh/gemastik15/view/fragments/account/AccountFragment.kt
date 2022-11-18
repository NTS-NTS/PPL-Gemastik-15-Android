package com.raassh.gemastik15.view.fragments.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auth0.android.jwt.JWT
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.DisabilityTypeAdapter
import com.raassh.gemastik15.databinding.FragmentAccountBinding
import com.raassh.gemastik15.utils.*
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
                    showSnackbar(
                        message = getString(R.string.theme_changed_message, theme),
                        anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                    )
                    viewModel.setTheme(getThemeKey(theme))
                }
            }

            btnLogout.setOnClickListener {
                root.showSnackbar(
                    message = getString(R.string.logging_out),
                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                )
                viewModel.logout()
            }

            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
            }

            btnModeration.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_moderationFragment)
            }
        }

        viewModel.apply {

            arrayAdapter.observe(viewLifecycleOwner) {
                binding?.etDarkMode?.setAdapter(it)
            }
        }

        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    val jwt = JWT(it)
                    val email = jwt.getClaim("email").asString()
                    viewModel.setToken(it)

                    viewModel.getProfile().observe(viewLifecycleOwner) { user ->
                        when (user) {
                            is Resource.Loading -> {
                            }
                            is Resource.Success -> {
                                binding?.apply {
                                    tvName.text = user.data?.name
                                    tvEmail.text = user.data?.email
                                    imgUser.loadImage(user.data?.profilePicture)

                                    if (!user.data?.city.isNullOrEmpty()) {
                                        tvCity.text = user.data?.city
                                        tvCity.visibility = View.VISIBLE
                                    }

                                    if (user.data?.disabilityTypes?.isNotEmpty() == true) {
                                        imgDisability.visibility = View.VISIBLE
                                        rvDisability.apply {
                                            adapter = DisabilityTypeAdapter().apply {
                                                submitList(user.data.disabilityTypes)
                                            }
                                            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                                                flexDirection = FlexDirection.ROW
                                                justifyContent = JustifyContent.FLEX_START
                                            }

                                            if (itemDecorationCount == 0) {
                                                addItemDecoration(FlexboxItemDecoration(requireContext()).apply {
                                                    setDrawable(AppCompatResources.getDrawable(context, R.drawable.divider))
                                                    setOrientation(FlexboxItemDecoration.BOTH)
                                                })
                                            }
                                        }
                                        rvDisability.visibility = View.VISIBLE
                                    }
                                }
                            }
                            is Resource.Error -> {
                                binding?.root?.showSnackbar(
                                    message = requireContext().translateErrorMessage(user.message),
                                    anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                )

                                requireActivity().checkAuthError(user.message)
                            }
                        }
                    }

                    binding?.btnResendVerification?.setOnClickListener {
                        if (email != null) {
                            viewModel.resendVerification(email).observe(viewLifecycleOwner) { response ->
                                when (response) {
                                    is Resource.Success -> {
                                        binding?.btnResendVerification?.isEnabled = true
                                        binding?.root?.showSnackbar(
                                            message = getString(R.string.verification_email_sent),
                                            anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
                                        )
                                    }
                                    is Resource.Error -> {
                                        binding?.btnResendVerification?.isEnabled = true
                                        binding?.root?.showSnackbar(
                                            message = requireContext().translateErrorMessage(response.message),
                                            anchor = binding?.root?.rootView?.findViewById(R.id.bottom_nav_view)
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
                    binding?.moderatorTag?.visibility = View.VISIBLE
                } else {
                    binding?.btnModeration?.visibility = View.GONE
                    binding?.moderatorTag?.visibility = View.GONE
                }
            }

            getIsVerified().observe(viewLifecycleOwner) {
                Log.d("AccountFragment", "getIsVerified: $it")
                if (it == true) {
                    binding?.btnResendVerification?.visibility = View.GONE
                    binding?.verifiedTag?.visibility = View.VISIBLE
                } else {
                    binding?.btnResendVerification?.visibility = View.VISIBLE
                    binding?.verifiedTag?.visibility = View.GONE
                }
            }

            theme.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding?.etDarkMode?.setText(getThemeString(it), false)
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