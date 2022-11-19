package com.raassh.gemastik15.view.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ChatRepository

class ChatViewModel(private val authenticationRepository: AuthenticationRepository, private val chatRepository: ChatRepository) : ViewModel() {
    private val chatId = MutableLiveData<String>()
    var userId = "0"
    private val receiverLiveData = MutableLiveData(userId)

    fun setChatId(chatId: String) {
        this.chatId.value = chatId
    }

    fun setReceiver(receiver: String) {
        this.receiverLiveData.value = receiver
    }

    fun getReceiver() = receiverLiveData.value ?: ""

    val messages = Transformations.switchMap(chatId) {
        chatRepository.getMessages(it).asLiveData()
    }

    fun sendMessage(message: String) = chatRepository.sendChatMessage(chatId.value ?: "", userId, message).asLiveData()

    fun startChat(receiver: String, message: String) = chatRepository.startChat(listOf(userId, receiver), userId, message).asLiveData()

    val username = Transformations.switchMap(receiverLiveData) {
        authenticationRepository.getUsername(it).asLiveData()
    }

    fun getChatId(userId: String) = chatRepository.getChatByUserId(userId)
}