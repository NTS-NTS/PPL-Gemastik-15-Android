package com.raassh.gemastik15.view.fragments.moderation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.raassh.gemastik15.adapter.UserReportsAdapter
import com.raassh.gemastik15.databinding.FragmentUserReportBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.checkAuthError
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UserReportFragment : Fragment() {
    private var binding: FragmentUserReportBinding? = null
    private val moderationViewModel by sharedViewModel<ModerationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserReportsAdapter().apply {
            onItemClickListener = { user ->
                Log.d("TAG", "onViewCreated: $user")
            }
        }

        binding?.apply {
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let {
                rvReports.addItemDecoration(it)
            }

            rvReports.adapter = adapter
        }

        moderationViewModel.usersReport.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)

                    adapter.submitList(it.data?.users)
                }
                is Resource.Error -> {
                    showLoading(false)

                    binding?.root?.showSnackbar(
                        requireContext().translateErrorMessage(it.message)
                    )

                    requireActivity().checkAuthError(it.message)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.apply {
            if (isLoading) {
                pbLoading.visibility = View.VISIBLE
                rvReports.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                rvReports.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}