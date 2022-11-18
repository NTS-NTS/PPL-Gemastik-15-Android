package com.raassh.gemastik15.view.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ChatRepository

class ChatViewModel(private val authenticationRepository: AuthenticationRepository, private val chatRepository: ChatRepository) : ViewModel() {
    private val chatId = MutableLiveData<String>()
    var username = ""

    fun setChatId(chatId: String) {
        this.chatId.value = chatId
    }

    val messages = Transformations.switchMap(chatId) {
        chatRepository.getMessages(it).asLiveData()
    }

    fun sendMessage(message: String) = chatRepository.sendChatMessage(chatId.value ?: "", username, message).asLiveData()

    fun startChat(receiver: String, message: String) = chatRepository.startChat(listOf(username, receiver), username, message).asLiveData()
}