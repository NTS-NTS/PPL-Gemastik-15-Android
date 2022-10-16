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
import com.raassh.gemastik15.databinding.PlaceItemBinding
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.loadImage
import com.raassh.gemastik15.utils.rounded
import com.raassh.gemastik15.utils.splitWithEmptyList

class PlaceAdapter : ListAdapter<PlaceEntity, PlaceAdapter.PlaceViewHolder>(DIFF_CALLBACK) {
    var onItemClickListener: ((PlaceEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaceViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
    )

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
        }

        fun bind(place: PlaceEntity) {
            binding.apply {
                tvPlaceName.text = place.name
                tvPlaceType.text = place.type
                ivPlaceImage.loadImage(place.image)
                rvPlaceTags.adapter = PlaceTagAdapter().apply {
                    submitList(place.facilities.splitWithEmptyList(","))
                }

                if (place.distance == -1.0) {
                    tvPlaceDistance.visibility = View.INVISIBLE
                    ivDot.visibility = View.INVISIBLE
                } else {
                    tvPlaceDistance.text = context.getString(R.string.distance, place.distance.rounded(2))
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