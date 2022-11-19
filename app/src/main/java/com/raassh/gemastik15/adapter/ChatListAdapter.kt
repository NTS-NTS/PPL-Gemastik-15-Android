package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.databinding.ChatListItemBinding
import com.raassh.gemastik15.local.db.ChatEntity
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.utils.loadImage
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel

class ChatListAdapter(private val chatListViewModel: IChatListViewModel) :
    ListAdapter<ChatEntity, ChatListAdapter.ChatListViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClickListener: ((ChatEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ChatListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_list_item, parent, false),
            parent.context as LifecycleOwner
        )

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    inner class ChatListViewHolder(itemView: View, private val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChatListItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(chat: ChatEntity) {
            binding.apply {
                ivAvatar.contentDescription = context.getString(R.string.avatar_desc, chat.users)
                chatListViewModel.getLastMessage(chat.id).observe(lifecycleOwner) {
                    tvLastMessage.text = it?.content
                }

                chatListViewModel.getProfilePicture(chat.users).observe(lifecycleOwner) {
                    when (it) {
                        is Resource.Success -> {
                            ivAvatar.loadImage(it.data)
                        }
                        else -> {
                            // intentionally empty
                        }
                    }
                }

                chatListViewModel.getUsername(chat.users).observe(lifecycleOwner) {
                    when (it) {
                        is Resource.Success -> {
                            tvUsername.text = it.data
                        }
                        else -> {
                            // intentionally empty
                        }
                    }
                }

                root.setOnClickListener {
                    onItemClickListener?.invoke(chat)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatEntity>() {
            override fun areItemsTheSame(
                oldItem: ChatEntity,
                newItem: ChatEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ChatEntity,
                newItem: ChatEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}