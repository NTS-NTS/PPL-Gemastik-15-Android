package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.DisabilityTypeItemBinding
import com.raassh.gemastik15.utils.translateDisabilityToView

class DisabilityTypeAdapter : ListAdapter<String, DisabilityTypeAdapter.DisabilityTypeViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisabilityTypeViewHolder {
        return DisabilityTypeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.disability_type_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DisabilityTypeViewHolder, position: Int) {
        val disability = getItem(position)
        holder.bind(disability)
    }

    inner class DisabilityTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = DisabilityTypeItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(disability: String) {
            binding.apply {
                tvDisability.text = context.translateDisabilityToView(disability)
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