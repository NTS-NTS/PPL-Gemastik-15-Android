package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.PlaceTagItemBinding

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

        fun bind(name: String) {
            binding.apply {
                tvTagName.text = name
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