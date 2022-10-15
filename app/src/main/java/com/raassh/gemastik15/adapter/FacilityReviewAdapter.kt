package com.raassh.gemastik15.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.color.MaterialColors
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
                val facilityName = context.translateDBtoViewName(facility.name)
                tvFacilityName.text = facilityName

                //quality is still double when it should be integer
                val qualityDrawable = context.getFacilityReviewDrawable(facility.quality.toInt())

                with(tvFacilityName) {
                    when(facility.quality.toInt()) {
                        0 -> {
                            val grey = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurfaceVariant, Color.BLACK)
                            setTextColor(grey)
                            qualityDrawable?.setTint(grey)
                        }
                        1 -> {
                            qualityDrawable?.setTint(MaterialColors.getColor(context, com.google.android.material.R.attr.colorError, Color.BLACK))
                        }
                        2 -> {
                            qualityDrawable?.setTint(MaterialColors.getColor(context, R.attr.colorGreen, Color.BLACK))
                        }
                        else -> null
                    }
                }
                imgReviewIcon.setImageDrawable(qualityDrawable)
                ivReviewWarning.visibility = if (facility.isTrusted) View.VISIBLE else View.GONE
                ivReviewWarning.setOnClickListener {
                    binding.root.showSnackbar(context.getString(R.string.varying_review_message, facilityName))
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