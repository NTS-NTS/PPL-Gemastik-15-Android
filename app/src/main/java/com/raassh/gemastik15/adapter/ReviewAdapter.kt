package com.raassh.gemastik15.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.databinding.FacilityReviewItemBinding
import com.raassh.gemastik15.databinding.ReviewCompactItemBinding
import com.raassh.gemastik15.utils.*

class ReviewAdapter : ListAdapter<ReviewData, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.review_compact_item, parent, false)
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ReviewCompactItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.rvReviewFacilities.addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
        }

        fun bind(review: ReviewData) {
            binding.apply {
                imgReviewProfile.loadImage(review.user.photo)
                tvReviewName.text = review.user.name
                tvReviewText.text = review.review
                rvReviewFacilities.adapter = ReviewFacilitiesAdapter().apply {
                    submitList(review.facilities.take(5))
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReviewData>() {
            override fun areItemsTheSame(oldItem: ReviewData, newItem: ReviewData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ReviewData, newItem: ReviewData): Boolean {
                return oldItem == newItem
            }
        }
    }
}