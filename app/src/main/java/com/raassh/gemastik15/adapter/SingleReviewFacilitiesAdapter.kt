package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.FacilityQualityItem
import com.raassh.gemastik15.databinding.FacilityReviewSmallItemBinding
import com.raassh.gemastik15.utils.*

class SingleReviewFacilitiesAdapter : ListAdapter<FacilityQualityItem, SingleReviewFacilitiesAdapter.ReviewFacilitiesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewFacilitiesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.facility_review_small_item, parent, false)
    )

    override fun onBindViewHolder(holder: ReviewFacilitiesViewHolder, position: Int) {
        val facility = getItem(position)
        holder.bind(facility)
    }

    inner class ReviewFacilitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FacilityReviewSmallItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(facility: FacilityQualityItem) {
            binding.apply {
                imgFacilityIcon.setImageDrawable(context.getFacilityDrawable(context.translateFacilitytoView(facility.facility)))

                imgFacilityReview.setImageDrawable(context.getFacilityReviewDrawable(facility.quality, false))
                cdFacilityReview.setCardBackgroundColor(context.getFacilityReviewColor(facility.quality))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FacilityQualityItem>() {
            override fun areItemsTheSame(oldItem: FacilityQualityItem, newItem: FacilityQualityItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FacilityQualityItem, newItem: FacilityQualityItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}