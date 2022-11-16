package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.SearchUserItem
import com.raassh.gemastik15.databinding.ChatListItemBinding

class ChatListAdapter :
    ListAdapter<SearchUserItem, ChatListAdapter.ChatListViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClickListener: ((SearchUserItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChatListItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(user: SearchUserItem) {
            binding.apply {
                tvUsername.text = user.username
                root.setOnClickListener {
                    onItemClickListener?.invoke(user)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchUserItem>() {
            override fun areItemsTheSame(
                oldItem: SearchUserItem,
                newItem: SearchUserItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SearchUserItem,
                newItem: SearchUserItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}