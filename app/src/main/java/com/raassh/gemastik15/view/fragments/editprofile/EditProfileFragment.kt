package com.raassh.gemastik15.view.fragments.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.UserProfile
import com.raassh.gemastik15.databinding.FragmentEditProfileBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : Fragment() {
    private val viewModel by viewModel<EditProfileViewModel>()
    private var binding: FragmentEditProfileBinding? = null
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private lateinit var cities: Array<String>

    private val launcherImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            val bitmap = requireContext().uriToBitmap(uri)
            binding?.ivProfilePicture?.setImageBitmap(bitmap)
            viewModel.setSelectedPicture(bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)

        cities = requireContext().getCities()

        binding?.apply {
            tvCity.apply {
                setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities))
                threshold = 1
            }

            btnEditProfile.setOnClickListener {
                tryEditProfile()
            }

            btnChangeProfilePicture.setOnClickListener {
                launcherImage.launch("image/*")
            }
        }

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            viewModel.setToken(it ?: "")
            viewModel.getProfile().observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)

                        showDetail(response.data)
                    }
                    is Resource.Error -> {
                        showLoading(false)

                        binding?.root?.showSnackbar(
                            requireContext().translateErrorMessage(response.message)
                        )

                        requireActivity().checkAuthError(response.message)
                    }
                }
            }
        }
    }

    private fun tryEditProfile() {
        with(binding ?: return) {
            val isNameValid = ilName.validate(getString(R.string.name))
            val isUsernameValid = ilUsername.validate(getString(R.string.username)) {
                if (it.contains(" ")) getString(R.string.username_invalid) else null
            }
            val isCityValid = ilCity.validate(getString(R.string.city), optional = true) {
                if (it.isNotBlank() && it !in cities) {
                    getString(R.string.city_not_valid)
                } else {
                    null
                }
            }

            if (!(isNameValid && isUsernameValid && isCityValid))
                return

            viewModel.editProfile(
                etName.text.toString(),
                etUsername.text.toString(),
                tvCity.text.toString()
            ).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> btnEditProfile.isEnabled = false
                    is Resource.Success -> {
                        btnEditProfile.isEnabled = true

                        binding?.root?.showSnackbar(getString(R.string.profile_updated))

                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        btnEditProfile.isEnabled = true

                        binding?.root?.showSnackbar(
                            requireContext().translateErrorMessage(it.message)
                        )

                        requireActivity().checkAuthError(it.message)
                    }
                }
            }
        }
    }

    private fun showDetail(userDetail: UserProfile?) {
        if (userDetail == null)
            return

        binding?.apply {
            etName.setText(userDetail.name)
            etUsername.setText(userDetail.username)
            tvCity.setText(userDetail.city)
            ivProfilePicture.loadImage(userDetail.profilePicture)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                groupEditProfile.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                groupEditProfile.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}