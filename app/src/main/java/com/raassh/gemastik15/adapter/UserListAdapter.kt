package com.raassh.gemastik15.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.response.SearchUserItem
import com.raassh.gemastik15.databinding.UserListItemBinding
import com.raassh.gemastik15.utils.loadImage

class UserListAdapter :
    ListAdapter<SearchUserItem, UserListAdapter.UserListViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClickListener: ((SearchUserItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        )

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = UserListItemBinding.bind(itemView)
        private val context = itemView.context

        fun bind(user: SearchUserItem) {
            binding.apply {
                tvUsername.text = user.username
                tvName.text = user.name
                ivAvatar.loadImage(user.profilePicture)
                ivAvatar.contentDescription = context.getString(R.string.avatar_desc, user.username)
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