package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.raassh.gemastik15.BuildConfig
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.PlaceItemBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.*

class PlaceAdapter(
    private val orientation: Int = RecyclerView.VERTICAL,
    private val inContribution: Boolean = false,
) : ListAdapter<PlaceEntity, PlaceAdapter.PlaceViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((PlaceEntity) -> Unit)? = null
    var btnOnItemClickListener: ((PlaceEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        if (orientation == RecyclerView.VERTICAL) {
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            view.layoutParams = ViewGroup.LayoutParams(
                convertDpToPixel(330, parent.context).toInt(),
                convertDpToPixel(280, parent.context).toInt()
            )
        }
        return PlaceViewHolder(view)
    }
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PlaceItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.apply {
                rvPlaceTags.layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
                root.setOnClickListener {
                    onItemClickListener?.invoke(getItem(adapterPosition))
                }
            }

            ViewCompat.replaceAccessibilityAction(
                itemView,
                AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
                context.getString(R.string.see_place_detail),
                null
            )
        }

        fun bind(place: PlaceEntity) {
            binding.apply {
                val facilityList = place.facilities.splitWithEmptyList(",")
                val facilitiesInfo = facilityList.map { it.splitWithEmptyList(":") }
                val facilities = facilitiesInfo.map {
                    StringBuilder().append(", ")
                        .append(context.translateDBtoViewName(it[0])).append(", ")
                        .append(context.getFacilityReviewDescription(it[1].toInt()))
                        .toString()
                }

                root.contentDescription = StringBuilder(place.name).append(", ")
                    .append(context.translateTypeName(place.type)).append(", ")
                    .append(context.getString(R.string.distance, place.distance.rounded(2)))
                    .append(facilities).toString()

                tvPlaceName.text = place.name
                tvPlaceType.text = context.translateTypeName(place.type)
                ivPlaceImage.loadImage(place.image, R.drawable.place_photo_placeholder_landscape, true)
                rvPlaceTags.adapter = PlaceTagAdapter().apply {
                    submitList(facilityList)
                }

                if (place.distance == -1.0) {
                    tvPlaceDistance.visibility = View.INVISIBLE
                    ivDot.visibility = View.INVISIBLE
                } else {
                    tvPlaceDistance.text = context.getString(R.string.distance, place.distance.rounded(2))
                }

                if (inContribution) {
                    btnAddContribution.visibility = View.VISIBLE
                    btnAddContribution.setOnClickListener {
                        btnOnItemClickListener?.invoke(getItem(adapterPosition))
                    }

                    ViewCompat.addAccessibilityAction(
                        itemView,
                        btnAddContribution.text
                    ) { _, _ ->
                        btnOnItemClickListener?.invoke(getItem(adapterPosition))
                        true
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlaceEntity>() {
            override fun areItemsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}