package com.raassh.gemastik15.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.ReviewData
import com.raassh.gemastik15.databinding.ReviewItemBinding
import com.raassh.gemastik15.utils.LinearSpaceItemDecoration
import com.raassh.gemastik15.utils.convertDpToPixel
import com.raassh.gemastik15.utils.loadImage

class ReviewAdapter(
    private val isCompact: Boolean = false,
    private val isSingle: Boolean = false
) : ListAdapter<ReviewData, ReviewAdapter.ReviewViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((ReviewData) -> Unit)? = null
    var onCardClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ReviewViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
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
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ReviewItemBinding.bind(itemView)

        init {
            binding.rvReviewFacilities.apply {
                if (itemDecorationCount == 0) {
                    addItemDecoration(LinearSpaceItemDecoration(16, RecyclerView.HORIZONTAL))
                }
            }

            binding.btnReviewReport.setOnClickListener {
                onItemClickListener?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(review: ReviewData) {
            binding.apply {
                if (isCompact) {
                    btnReviewReport.visibility = View.GONE
                    tvReviewText.maxLines = 3
                    tvReviewText.ellipsize = TextUtils.TruncateAt.END
                    root.isClickable = true
                    root.setOnClickListener {
                        onCardClickListener?.invoke(adapterPosition)
                    }
                }
                else {
                    btnReviewReport.visibility = View.VISIBLE
                    root.isClickable = false
                }
                imgReviewProfile.loadImage(review.user.profilePicture)
                tvReviewName.text = review.user.name
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