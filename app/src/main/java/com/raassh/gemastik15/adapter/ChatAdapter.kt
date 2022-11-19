package com.raassh.gemastik15.adapter

import android.graphics.Color
import android.graphics.Color.WHITE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ChatItemBinding
import com.raassh.gemastik15.local.db.MessageEntity
import com.raassh.gemastik15.utils.getColorFromAttr
import com.raassh.gemastik15.utils.toDateText

class ChatAdapter : ListAdapter<MessageEntity, ChatAdapter.ChatViewHolder>(
    DIFF_CALLBACK
) {
    var username = ""
    var previousChat: MessageEntity? = null

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
                    llChat.gravity = Gravity.END
                    root.gravity = Gravity.END

                    cdChat.background = ContextCompat.getDrawable(context, R.drawable.chat_background_primary)
                    tvContent.setTextColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnPrimary))
                    tvTime.setTextColor(context.getColorFromAttr(com.google.android.material.R.attr.colorSurfaceVariant))
                } else {
                    tvContent.gravity = Gravity.START
                    tvTime.gravity = Gravity.START
                    llChat.gravity = Gravity.START
                    root.gravity = Gravity.START

                    cdChat.background = ContextCompat.getDrawable(context, R.drawable.chat_background_surface)
                    tvContent.setTextColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnSurface))
                    tvTime.setTextColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnSurfaceVariant))
                }

//                previousChat = chat
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