package com.raassh.gemastik15.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.ContributionUserData
import com.raassh.gemastik15.api.response.PlacesItem
import com.raassh.gemastik15.databinding.ReviewHistoryItemBinding
import com.raassh.gemastik15.utils.LinearSpaceItemDecoration
import com.raassh.gemastik15.utils.convertDpToPixel
import com.raassh.gemastik15.utils.rounded
import com.raassh.gemastik15.utils.translatePlaceTypeNameToView

class ReviewHistoryAdapter(
    private val isCompact: Boolean = false,
    private val isSingle: Boolean = false
) : ListAdapter<ContributionUserData, ReviewHistoryAdapter.ReviewHistoryViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((ContributionUserData, ReviewHistoryItemBinding) -> Unit)? = null
    var onPlaceClickListener: ((PlacesItem) -> Unit)? = null
    var onCardClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ReviewHistoryViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_history_item, parent, false)
        if (!isCompact) {
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            view.layoutParams = ViewGroup.LayoutParams(
                if (isSingle) ViewGroup.LayoutParams.MATCH_PARENT
                else convertDpToPixel(340, parent.context).toInt(),
                convertDpToPixel(212, parent.context).toInt()
            )
        }
        return ReviewHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewHistoryViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ReviewHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ReviewHistoryItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.rvReviewFacilities.apply {
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }

            binding.btnOption.setOnClickListener {
                onItemClickListener?.invoke(getItem(adapterPosition), binding)
            }

            binding.llPlace.setOnClickListener {
                onPlaceClickListener?.invoke(getItem(adapterPosition).place)
            }
        }

        fun bind(review: ContributionUserData) {
            binding.apply {
                if (isCompact) {
                    tvReviewText.maxLines = 4
                    tvReviewText.ellipsize = TextUtils.TruncateAt.END
                    root.isClickable = true
                    root.setOnClickListener {
                        onCardClickListener?.invoke(adapterPosition)
                    }
                }
                else {
                    root.isClickable = false
                }
                tvPlaceName.text = review.place.name
                tvPlaceType.text = context.translatePlaceTypeNameToView(review.place.kind)
                if (review.place.distance == -1.0) {
                    tvPlaceDistance.visibility = View.INVISIBLE
                    ivDot.visibility = View.INVISIBLE
                } else {
                    tvPlaceDistance.text = context.getString(R.string.distance, review.place.distance.rounded(2))
                }
                tvReviewText.text = review.review
                rvReviewFacilities.adapter = SingleReviewFacilitiesAdapter(
                    if (isCompact) review.facilities.size else null
                ).apply {
                    submitList(review.facilities)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContributionUserData>() {
            override fun areItemsTheSame(oldItem: ContributionUserData, newItem: ContributionUserData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ContributionUserData, newItem: ContributionUserData): Boolean {
                return oldItem == newItem
            }
        }
    }
}