package com.raassh.gemastik15.view.fragments.searchuser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.adapter.UserListAdapter
import com.raassh.gemastik15.databinding.FragmentUserSearchResultBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserSearchResultFragment : Fragment() {
    private val viewModel by sharedViewModel<SearchUserViewModel>()
    private var binding: FragmentUserSearchResultBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSearchResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userAdapter = UserListAdapter().apply {
            onItemClickListener = {
                val action =
                    SearchUserFragmentDirections.actionSearchUserFragmentToUserProfileFragment(it.id)
                findNavController().navigate(action)
            }
        }

        binding?.apply {
            rvUsers.adapter = userAdapter
        }

        showLoading(false)

        viewModel.apply {
            users.observe(viewLifecycleOwner) {
                Log.d("TAG", "onViewCreated: $it")
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        userAdapter.submitList(it.data)

                        if (it.data.isNullOrEmpty()) {
                            showEmpty(true)
                        } else {
                            showEmpty(false)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showEmpty(true)

                        binding?.root?.showSnackbar(
                            message = requireContext().translateErrorMessage(it.message),
                        )
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
                rvUsers.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                rvUsers.visibility = View.VISIBLE
            }
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        binding?.apply {
            if (isEmpty) {
                tvNoUser.visibility = View.VISIBLE
                rvUsers.visibility = View.GONE
            } else {
                tvNoUser.visibility = View.GONE
                rvUsers.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}