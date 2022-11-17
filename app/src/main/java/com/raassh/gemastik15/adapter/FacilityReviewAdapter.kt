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
import com.raassh.gemastik15.databinding.FacilityReviewItemBinding
import com.raassh.gemastik15.utils.getFacilityReviewDescription
import com.raassh.gemastik15.utils.getFacilityReviewDrawable
import com.raassh.gemastik15.utils.showSnackbar
import com.raassh.gemastik15.utils.translateFacilitytoView

class FacilityReviewAdapter : ListAdapter<FacilitiesItem, FacilityReviewAdapter.FacilityReviewViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((FacilitiesItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FacilityReviewViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.facility_review_item, parent, false)
    )

    override fun onBindViewHolder(holder: FacilityReviewViewHolder, position: Int) {
        val facilityReview = getItem(position)
        holder.bind(facilityReview)
    }

    inner class FacilityReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = FacilityReviewItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(facility: FacilitiesItem) {
            binding.apply {
                val facilityName = context.translateFacilitytoView(facility.name)
                tvFacilityName.text = facilityName

                //quality is still double when it should be integer
                val qualityDrawable = context.getFacilityReviewDrawable(facility.quality.toInt())

                if (facility.quality.toInt() == 0) {
                    val grey = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurfaceVariant, Color.BLACK)
                    tvFacilityName.setTextColor(grey)
                }

                imgReviewIcon.setImageDrawable(qualityDrawable)
                ivReviewWarning.visibility = if (facility.isTrusted) View.VISIBLE else View.GONE
                ivReviewWarning.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
                ivReviewWarning.setOnClickListener {
                    binding.root.showSnackbar(context.getString(R.string.varying_review_message, facilityName))
                }

                itemView.contentDescription =
                    StringBuilder()
                        .append(facilityName).append(", ")
                        .append(context.getFacilityReviewDescription(facility.quality.toInt())).append(", ")
                        .append(if (facility.isTrusted) context.getString(R.string.varying_reviews) else "")
                        .toString()
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FacilitiesItem>() {
            override fun areItemsTheSame(oldItem: FacilitiesItem, newItem: FacilitiesItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: FacilitiesItem, newItem: FacilitiesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}