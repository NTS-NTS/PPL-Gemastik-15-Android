package com.raassh.gemastik15.view.fragments.detailuserreport

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.DataDetailReportUserResponse
import com.raassh.gemastik15.databinding.FragmentDetailUserReportBinding
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.checkAuthError
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateErrorMessage
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailUserReportFragment : Fragment() {
    private var binding: FragmentDetailUserReportBinding? = null
    private val viewModel by viewModel<DetailUserReportViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailUserReportBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reported = DetailUserReportFragmentArgs.fromBundle(requireArguments()).reported
        val ctx = requireContext()

        // TODO: change this
        val reasonList = arrayOf("test1", "test2", "test3")

        binding?.apply {
            root.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

            tvName.text = reported.name
            tvUsername.text = reported.username
            tvReportCount.text =reported.reportCount.toString()

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
                AlertDialog.Builder(ctx)
                    .setTitle(R.string.moderate_contribution_reason)
                    .setSingleChoiceItems(reasonList, -1) { _, which ->
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
                viewModel.setParams(it, reported.userId)
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

    private fun showDetail(data: DataDetailReportUserResponse?, ctx: Context) {
        if (data == null) {
            return
        }

        Log.d("TAG", "showDetail: $data")

        val reason =
            data.reportReason.distinct().joinToString(", ")

        binding?.apply {
            tvReportReason.text = reason
            tvEmail.text = data.email
        }

    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            if (loading) {
                pbLoading.visibility = View.VISIBLE
                tvUsername.visibility = View.GONE
                tvName.visibility = View.GONE
                tvEmail.visibility = View.GONE
                tvReportCount.visibility = View.GONE
                tvReportReason.visibility = View.GONE
                btnModerate.visibility = View.GONE
                btnDismiss.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                tvUsername.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
                tvEmail.visibility = View.VISIBLE
                tvReportCount.visibility = View.VISIBLE
                tvReportReason.visibility = View.VISIBLE
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