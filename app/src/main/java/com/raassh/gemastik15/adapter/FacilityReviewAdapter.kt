package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.FacilitiesItem
import com.raassh.gemastik15.databinding.FacilityReviewItemBinding
import com.raassh.gemastik15.databinding.PlaceItemBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.*

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
                tvFacilityName.text = facility.name

                //quality is still double when it should be integer
                val qualityDrawable = context.getFacilityReviewDrawable(facility.quality.toInt())
                tvFacilityName.setCompoundDrawables(qualityDrawable, null, null, null)
                ivReviewWarning.visibility = if (facility.isTrusted) View.VISIBLE else View.GONE
                ivReviewWarning.setOnClickListener {
                   binding.root.showSnackbar("The reviews for this facility vary")
                }
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