package com.raassh.gemastik15.view.fragments.reportreview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ReviewReasonAdapter
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.databinding.FragmentReportReviewBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.getReviewReason
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReportReviewFragment : Fragment() {
    private var binding: FragmentReportReviewBinding? = null
    private val viewModel by viewModel<ReportReviewViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private lateinit var review: ReviewData
    private val reasons = listOf(
        "spam", "off_topic", "conflict_of_interest", "inappropriate", "harassment", "hate_speech", "private_information", "not_helpful"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportReviewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.root?.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

        review = ReportReviewFragmentArgs.fromBundle(requireArguments()).review
        Log.d("review", review.toString())
        binding?.rvReasons?.apply {
            adapter = ReviewReasonAdapter().apply {
                submitList(reasons)
                onItemClickListener = { reason ->
                    val reasonString = context.getReviewReason(reason).lowercase()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.report_review))
                        .setMessage(getString(R.string.report_review_confirmation, reasonString))
                        .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                            sendReport(reason)
                            dialog.dismiss()
                        }
                        .setNegativeButton(getString(R.string.no)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        binding?.btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendReport(reason: String) {
        sharedViewModel.apply {
            getToken().observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {
                    viewModel.setToken(it)
                } else {
                    findNavController().navigateUp()
                }
            }
        }

        viewModel.token.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                trySubmitReport(it, review.place_id, review.user.id, reason)
            }
        }
    }

    private fun trySubmitReport(token: String?, placeId: String, userId: String, reason: String) {
        if (token.isNullOrEmpty()) {
            return
        }

        viewModel.submitReport(token, placeId, userId, reason)
            .observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            findNavController().navigateUp()
                            binding?.root?.showSnackbar(getString(R.string.report_submitted))
                        }
                        is Resource.Error -> {
                            findNavController().navigateUp()

                            binding?.root?.showSnackbar(
                                requireContext().translateErrorMessage(response.message)
                            )
                        }
                    }
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}