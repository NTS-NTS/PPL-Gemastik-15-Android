package com.raassh.gemastik15.view.fragments.chatlist

import androidx.lifecycle.LiveData
import com.raassh.gemastik15.local.db.MessageEntity
import com.raassh.gemastik15.utils.Resource

interface IChatListViewModel {
    fun getLastMessage(chatId: String): LiveData<MessageEntity>
    fun getProfilePicture(userId: String): LiveData<Resource<String>>
    fun getUsername(userId: String): LiveData<Resource<String>>
}