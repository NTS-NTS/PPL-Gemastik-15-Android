package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.OptionTagItemBinding
import com.raassh.gemastik15.utils.getFacilityDrawable
import com.raassh.gemastik15.utils.translateDBtoViewName

class OptionTagAdapter : ListAdapter<String, OptionTagAdapter.OptionTagViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionTagViewHolder {
        return OptionTagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.option_tag_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OptionTagViewHolder, position: Int) {
        val tag = getItem(position)
        holder.bind(tag)
    }

    inner class OptionTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = OptionTagItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(name: String) {
            binding.apply {
                tvOptionName.text = context.translateDBtoViewName(name)
                ivOptionIcon.setImageDrawable(context.getFacilityDrawable(name))
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