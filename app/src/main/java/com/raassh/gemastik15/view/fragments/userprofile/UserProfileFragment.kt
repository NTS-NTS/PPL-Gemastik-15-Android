package com.raassh.gemastik15.view.fragments.userprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.DisabilityTypeAdapter
import com.raassh.gemastik15.api.response.DetailUserResponseData
import com.raassh.gemastik15.databinding.FragmentUserProfileBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.loadImage
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment() {
    private val viewModel by viewModel<UserProfileViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private var binding: FragmentUserProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = UserProfileFragmentArgs.fromBundle(requireArguments()).userId

        binding?.apply {
            btnChat.setOnClickListener {
                val action = UserProfileFragmentDirections.actionUserProfileFragmentToChatFragment()
                action.receiver = userId
                findNavController().navigate(action)
            }
        }

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                viewModel.token = it
            }
        }

        viewModel.apply {
            getUser(userId).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        setDetail(it.data)
                    }
                    is Resource.Error -> {
                        binding?.root?.showSnackbar(
                            requireContext().translateErrorMessage(it.message)
                        )

                        findNavController().navigateUp()
                    }
                }
            }
        }

    }

    private fun setDetail(data: DetailUserResponseData?) {
        if (data == null) return

        binding?.apply {
            imgUser.loadImage(data.profilePicture)
            tvName.text = data.name
            tvUsername.text = data.username

            if (!data.city.isNullOrEmpty()) {
                tvCity.text = data.city
                tvCity.visibility = View.VISIBLE
            }

            if (data.disabilityTypes.isNotEmpty()) {
                imgDisability.visibility = View.VISIBLE
                rvDisability.apply {
                    adapter = DisabilityTypeAdapter().apply {
                        submitList(data.disabilityTypes)
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

            btnChat.setOnClickListener {
                val action = UserProfileFragmentDirections.actionUserProfileFragmentToChatFragment()
                action.receiver = data.username
                findNavController().navigate(action)
            }

            btnReport.setOnClickListener {
                // TODO: change this and ask reason
                viewModel.reportUser(data.id, "test").observe(viewLifecycleOwner) {
                    Log.d("TAG", "setDetail: $it")
                }
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                btnChat.visibility = View.GONE
                btnReport.visibility = View.GONE
                cdUser.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                btnChat.visibility = View.VISIBLE
                btnReport.visibility = View.VISIBLE
                cdUser.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}