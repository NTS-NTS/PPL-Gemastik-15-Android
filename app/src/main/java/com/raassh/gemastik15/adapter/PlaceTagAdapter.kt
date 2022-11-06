package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.PlaceTagItemBinding
import com.raassh.gemastik15.utils.getFacilityReviewDrawable
import com.raassh.gemastik15.utils.translateFacilitytoView

class PlaceTagAdapter : ListAdapter<String, PlaceTagAdapter.PlaceTagViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceTagViewHolder {
        return PlaceTagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.place_tag_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaceTagViewHolder, position: Int) {
        val tag = getItem(position)
        holder.bind(tag)
    }

    inner class PlaceTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PlaceTagItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(place: String) {
            val placeInfo = place.split(":").toTypedArray()
            binding.apply {
                tvTagName.text = context.translateFacilitytoView(placeInfo[0])
                ivTagIcon.setImageDrawable(context.getFacilityReviewDrawable(placeInfo[1].toInt()))
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}