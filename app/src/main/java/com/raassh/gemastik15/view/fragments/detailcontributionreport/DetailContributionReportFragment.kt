package com.raassh.gemastik15.view.fragments.detailcontributionreport

import android.content.Context
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.SingleReviewFacilitiesAdapter
import com.raassh.gemastik15.api.response.DataDetailReportContributionResponse
import com.raassh.gemastik15.databinding.FragmentDetailContributionReportBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailContributionReportFragment : Fragment() {
    private var binding: FragmentDetailContributionReportBinding? = null
    private val viewModel by viewModel<DetailContributionReportViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailContributionReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reported = DetailContributionReportFragmentArgs.fromBundle(requireArguments()).reported
        val ctx = requireContext()

        val reasonList = arrayOf(
            "spam", "off_topic", "conflict_of_interest", "inappropriate", "harassment", "hate_speech", "private_information", "not_helpful"
        )

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            tvPlace.text = reported.place
            tvUser.text = reported.user
            tvReportCount.text = reported.reportCount.toString()

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnDismiss.setOnClickListener {
                viewModel.dismiss().observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                        is Resource.Success -> {
                            findNavController().navigateUp()
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

            btnModerate.setOnClickListener {
                MaterialAlertDialogBuilder(ctx)
                    .setTitle(R.string.moderate_contribution_reason)
                    .setSingleChoiceItems(
                        reasonList.map { context?.getReviewReason(it) }.toTypedArray(),
                        -1) { _, which ->
                       viewModel.setReason(reasonList[which])
                    }
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        dialog.dismiss()
                        viewModel.moderate().observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Resource.Loading -> {
                                    showLoading(true)
                                }
                                is Resource.Success -> {
                                    findNavController().navigateUp()
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
                    .setNegativeButton(R.string.no) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        sharedViewModel.getToken().observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                viewModel.setParams(it, reported.placeId, reported.userId)
                viewModel.getDetail().observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Success -> {
                            showLoading(false)

                            showDetail(response.data, ctx)
                        }
                        is Resource.Error -> {
                            binding?.root?.showSnackbar(
                                requireContext().translateErrorMessage(response.message)
                            )

                            requireActivity().checkAuthError(response.message)

                            findNavController().navigateUp()
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }
        }
    }

    private fun showDetail(data: DataDetailReportContributionResponse?, ctx: Context) {
        if (data == null) {
            return
        }

        Log.d("DetailContribution", data.reportReason.distinct().toString())
        Log.d("DetailContribution", data.reportReason.distinct().map { ctx.getReviewReason(it) }.toString())

        val reason = data.reportReason.distinct().joinToString(", ") {
            ctx.getReviewReason(it)
        }

        binding?.apply {
            tvReportReason.text = reason
            tvReportReview.text = data.review
            rvReportedFacilities.apply {
                adapter = SingleReviewFacilitiesAdapter().apply {
                    submitList(data.facilities)
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
        }

    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                tvUser.visibility = View.GONE
                tvPlace.visibility = View.GONE
                tvReportCount.visibility = View.GONE
                tvReportReason.visibility = View.GONE
                tvReportReview.visibility = View.GONE
                rvReportedFacilities.visibility = View.GONE
                btnModerate.visibility = View.GONE
                btnDismiss.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                tvUser.visibility = View.VISIBLE
                tvPlace.visibility = View.VISIBLE
                tvReportCount.visibility = View.VISIBLE
                tvReportReason.visibility = View.VISIBLE
                tvReportReview.visibility = View.VISIBLE
                rvReportedFacilities.visibility = View.VISIBLE
                btnModerate.visibility = View.VISIBLE
                btnDismiss.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}