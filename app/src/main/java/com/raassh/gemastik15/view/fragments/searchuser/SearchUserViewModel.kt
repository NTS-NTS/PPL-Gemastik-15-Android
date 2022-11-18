package com.raassh.gemastik15.view.fragments.searchuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ChatRepository
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel

class SearchUserViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val chatRepository: ChatRepository
) : ViewModel(),
    IChatListViewModel {
    private val query = MutableLiveData<String>()

    fun setQuery(query: String) {
        this.query.value = query
    }

    val users = Transformations.switchMap(query) {
        authenticationRepository.searchUser(it).asLiveData()
    }

    val chats = Transformations.switchMap(query) {
        chatRepository.searchChat(it).asLiveData()
    }

    override fun getLastMessage(chatId: String) = chatRepository.getLastMessage(chatId).asLiveData()

    override fun getProfilePicture(username: String) = authenticationRepository.getUserPicture(username).asLiveData()
}