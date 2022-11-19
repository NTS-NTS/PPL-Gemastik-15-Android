package com.raassh.gemastik15.view.fragments.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ChatRepository

class ChatListViewModel(private val authenticationRepository: AuthenticationRepository, private val chatRepository: ChatRepository) : ViewModel(),
    IChatListViewModel {

    val chats = chatRepository.getChats().asLiveData()

    override fun getLastMessage(chatId: String) = chatRepository.getLastMessage(chatId).asLiveData()

    override fun getProfilePicture(userId: String) = authenticationRepository.getUserPicture(userId).asLiveData()

    override fun getUsername(userId: String) = authenticationRepository.getUsername(userId).asLiveData()
}