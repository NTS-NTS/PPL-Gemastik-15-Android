package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.ItemReportContributionResponse
import com.raassh.gemastik15.databinding.ContributionReportItemBinding

class ContributionReportsAdapter :
    ListAdapter<ItemReportContributionResponse, ContributionReportsAdapter.ContributionReportsViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClickListener: ((ItemReportContributionResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContributionReportsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contribution_report_item, parent, false)
        )

    override fun onBindViewHolder(holder: ContributionReportsViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
    }

    inner class ContributionReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ContributionReportItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(report: ItemReportContributionResponse) {
            binding.apply {
                tvPlace.text = report.place
                tvUser.text = report.user
                tvReportCount.text = context.getString(R.string.report_count, report.reportCount)
                root.setOnClickListener {
                    onItemClickListener?.invoke(report)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemReportContributionResponse>() {
            override fun areItemsTheSame(
                oldItem: ItemReportContributionResponse,
                newItem: ItemReportContributionResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemReportContributionResponse,
                newItem: ItemReportContributionResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}