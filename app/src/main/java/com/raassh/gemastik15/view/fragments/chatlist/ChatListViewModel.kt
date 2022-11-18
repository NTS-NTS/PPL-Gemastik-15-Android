package com.raassh.gemastik15.view.fragments.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class ChatListViewModel(private val authenticationRepository: AuthenticationRepository) : ViewModel() {
    fun searchUser(username: String) = authenticationRepository.searchUser(username).asLiveData()
}