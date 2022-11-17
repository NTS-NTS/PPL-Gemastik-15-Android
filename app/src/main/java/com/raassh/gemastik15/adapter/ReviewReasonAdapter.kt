package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ReviewReasonItemBinding
import com.raassh.gemastik15.utils.getReviewReason
import com.raassh.gemastik15.utils.getReviewReasonDescription

class ReviewReasonAdapter : ListAdapter<String, ReviewReasonAdapter.ReviewReasonViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewReasonViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.review_reason_item, parent, false)
    )

    override fun onBindViewHolder(holder: ReviewReasonViewHolder, position: Int) {
        val reason = getItem(position)
        holder.bind(reason)
    }

    inner class ReviewReasonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ReviewReasonItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(reason: String) {
            binding.apply {
                tvReason.text = context.getReviewReason(reason)
                tvReasonDescription.text = context.getReviewReasonDescription(reason)
                root.setOnClickListener() {
                    onItemClickListener?.invoke(reason)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}