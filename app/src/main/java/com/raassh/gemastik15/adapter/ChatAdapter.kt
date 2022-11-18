package com.raassh.gemastik15.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ChatItemBinding
import com.raassh.gemastik15.local.db.MessageEntity
import com.raassh.gemastik15.utils.toDateText

class ChatAdapter : ListAdapter<MessageEntity, ChatAdapter.ChatViewHolder>(
    DIFF_CALLBACK
) {
    var username = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item, parent, false)
        )

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChatItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(chat: MessageEntity) {
            binding.apply {
                tvContent.text = chat.content
                tvTime.text = chat.timestamp.toDateText()

                if (chat.sender == username) {
                    tvContent.gravity = Gravity.END
                    tvTime.gravity = Gravity.END
                } else {
                    tvContent.gravity = Gravity.START
                    tvTime.gravity = Gravity.START
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageEntity>() {
            override fun areItemsTheSame(
                oldItem: MessageEntity,
                newItem: MessageEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MessageEntity,
                newItem: MessageEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}