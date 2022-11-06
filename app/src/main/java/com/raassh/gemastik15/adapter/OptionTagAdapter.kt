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
import com.raassh.gemastik15.utils.translateFacilitytoView

class OptionTagAdapter(private val size: Int) : ListAdapter<String, OptionTagAdapter.OptionTagViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionTagViewHolder {
        return OptionTagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.option_tag_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: OptionTagViewHolder, position: Int) {
        val tag = getItem(position)

        if (position < 7) {
            holder.bind(tag)
        } else if (position == 7) {
            holder.bind("others")
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    inner class OptionTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = OptionTagItemBinding.bind(itemView)
        private val context = itemView.context

        init {
            binding.root.isFocusable = false
        }

        fun bind(name: String) {
            binding.apply {

                if (name == "others") {
                    ivOptionIcon.visibility = View.GONE

                    val restCount = size - 7
                    tvOptionName.text = context.getString(R.string.overflow_count, restCount)
                } else {
                    ivOptionIcon.setImageDrawable(context.getFacilityDrawable(name))
                    tvOptionName.text = context.translateFacilitytoView(name)
                }
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