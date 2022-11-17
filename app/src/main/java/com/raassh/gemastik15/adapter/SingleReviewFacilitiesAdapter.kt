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

class SingleReviewFacilitiesAdapter(
    private val size: Int? = null
) : ListAdapter<FacilityQualityItem, SingleReviewFacilitiesAdapter.ReviewFacilitiesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewFacilitiesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.facility_review_small_item, parent, false)
    )

    override fun onBindViewHolder(holder: ReviewFacilitiesViewHolder, position: Int) {
        val facility = getItem(position)

        if (size != null) {
            if (position < 4) {
                holder.bind(facility)
                holder.itemView.visibility = View.VISIBLE
            } else if (position == 4) {
                holder.bind(null)
                holder.itemView.visibility = View.VISIBLE
            } else {
                holder.itemView.visibility = View.GONE
            }
        } else {
            holder.bind(facility)
        }
    }

    inner class ReviewFacilitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FacilityReviewSmallItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(facility: FacilityQualityItem?) {
            binding.apply {
                if (facility != null) {
                    tvRestCount.visibility = View.GONE
                    imgFacilityIcon.visibility = View.VISIBLE
                    cdFacilityReview.visibility = View.VISIBLE

                    imgFacilityIcon.setImageDrawable(context.getFacilityDrawable(context.translateFacilitytoView(facility.facility)))

                    imgFacilityReview.setImageDrawable(context.getFacilityReviewDrawable(facility.quality, false))
                    cdFacilityReview.setCardBackgroundColor(context.getFacilityReviewColor(facility.quality))

                    root.setOnClickListener{
                        root.showSnackbar(context.getString(
                            R.string.single_facility_review_description,
                            context.translateFacilitytoView(facility.facility),
                            context.getFacilityQuality(facility.quality)))
                    }
                } else {
                    val restCount = size!! - 4
                    tvRestCount.text = StringBuilder().append("+").append(restCount)

                    tvRestCount.visibility = View.VISIBLE
                    imgFacilityIcon.visibility = View.GONE
                    cdFacilityReview.visibility = View.GONE

                    root.setOnClickListener(null)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FacilityQualityItem>() {
            override fun areItemsTheSame(oldItem: FacilityQualityItem, newItem: FacilityQualityItem): Boolean {
                return oldItem.facility == newItem.facility
            }

            override fun areContentsTheSame(oldItem: FacilityQualityItem, newItem: FacilityQualityItem): Boolean {
                return oldItem.facility == newItem.facility
            }
        }
    }
}