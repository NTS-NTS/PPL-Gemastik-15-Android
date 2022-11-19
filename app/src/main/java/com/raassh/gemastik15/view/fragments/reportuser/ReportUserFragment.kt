package com.raassh.gemastik15.view.fragments.reportuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raassh.gemastik15.R
import com.raassh.gemastik15.adapter.ReasonAdapter
import com.raassh.gemastik15.databinding.FragmentReportUserBinding
import com.raassh.gemastik15.utils.*
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportUserFragment : Fragment() {
    private var binding: FragmentReportUserBinding? = null
    private val viewModel by viewModel<ReportUserViewModel>()
    private val sharedViewModel by sharedViewModel<DashboardViewModel>()
    private lateinit var userId: String
    private val reasons = listOf(
        "spam", "self_harm", "impersonation", "inappropriate", "harassment", "hate_speech", "private_information", "not_like"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.root?.applyInsetter { type(statusBars = true, navigationBars = true) { padding() } }

        userId = ReportUserFragmentArgs.fromBundle(requireArguments()).userId

        binding?.rvReasons?.apply {
            adapter = ReasonAdapter(true).apply {
                submitList(reasons)
                onItemClickListener = { reason ->
                    val reasonString = context.getUserReason(reason).lowercase()
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.report_user))
                        .setMessage(getString(R.string.report_user_confirmation, reasonString))
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
                trySubmitReport(it, userId, reason)
            }
        }
    }

    private fun trySubmitReport(token: String?, userId: String, reason: String) {
        if (token.isNullOrEmpty()) {
            return
        }

        viewModel.reportUser(token, userId, reason)
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}