package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.ItemReportUserResponse
import com.raassh.gemastik15.databinding.UserReportItemBinding

class UserReportsAdapter :
    ListAdapter<ItemReportUserResponse, UserReportsAdapter.UserReportsViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClickListener: ((ItemReportUserResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserReportsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_report_item, parent, false)
        )

    override fun onBindViewHolder(holder: UserReportsViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
    }

    inner class UserReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserReportItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(report: ItemReportUserResponse) {
            binding.apply {
                tvName.text = context.getString(R.string.reported_name, report.name)
                tvUsername.text = context.getString(R.string.reported_username, report.username)
                tvReportCount.text = context.getString(R.string.report_count, report.reportCount)
                root.setOnClickListener {
                    onItemClickListener?.invoke(report)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemReportUserResponse>() {
            override fun areItemsTheSame(
                oldItem: ItemReportUserResponse,
                newItem: ItemReportUserResponse
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemReportUserResponse,
                newItem: ItemReportUserResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}