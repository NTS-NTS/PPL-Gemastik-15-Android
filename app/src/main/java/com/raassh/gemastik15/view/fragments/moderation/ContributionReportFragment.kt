package com.raassh.gemastik15.view.fragments.moderation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.raassh.gemastik15.adapter.ContributionReportsAdapter
import com.raassh.gemastik15.databinding.FragmentContributionReportBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.checkAuthError
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ContributionReportFragment : Fragment() {
    private var binding: FragmentContributionReportBinding? = null
    private val moderationViewModel by sharedViewModel<ModerationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContributionReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ContributionReportsAdapter().apply {
            onItemClickListener = { contribution ->
                Log.d("ContributionReport", "onViewCreated: $contribution")
                findNavController().navigate(
                    ModerationFragmentDirections.actionModerationFragmentToDetailContributionReportFragment(
                        contribution
                    )
                )
            }
        }

        binding?.apply {
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let {
                rvReports.addItemDecoration(it)
            }

            rvReports.adapter = adapter
        }

        moderationViewModel.contributionsReport.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)

                    if (it.data?.contributions?.isNotEmpty() == true) {
                        adapter.submitList(it.data.contributions)
                        showEmpty(false)
                    } else {
                        showEmpty(true)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showEmpty(true)

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

    private fun showEmpty(isEmpty: Boolean) {
        binding?.apply {
            if (isEmpty) {
                tvNoReport.visibility = View.VISIBLE
                rvReports.visibility = View.GONE
            } else {
                tvNoReport.visibility = View.GONE
                rvReports.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}