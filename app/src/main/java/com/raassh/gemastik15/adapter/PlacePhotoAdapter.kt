package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.PlacePhotoItemBinding
import com.raassh.gemastik15.utils.loadImage

class PlacePhotoAdapter : ListAdapter<String, PlacePhotoAdapter.PlacePhotoViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlacePhotoViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.place_photo_item, parent, false)
    )

    override fun onBindViewHolder(holder: PlacePhotoViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
    }

    inner class PlacePhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = PlacePhotoItemBinding.bind(itemView)

        fun bind(photoUrl: String) {
            binding.imgPlacePhoto.loadImage(photoUrl, isGmpRequest = true)
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