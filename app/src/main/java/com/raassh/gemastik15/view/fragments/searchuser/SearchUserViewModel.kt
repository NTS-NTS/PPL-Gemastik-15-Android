package com.raassh.gemastik15.view.fragments.searchuser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.ChatRepository
import com.raassh.gemastik15.view.fragments.chatlist.IChatListViewModel
import kotlinx.coroutines.flow.map

class SearchUserViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val chatRepository: ChatRepository
) : ViewModel(),
    IChatListViewModel {
    val query = MutableLiveData<String>()

    fun setQuery(query: String) {
        this.query.value = query
    }

    val users = Transformations.switchMap(query) {
        authenticationRepository.searchUser(it).asLiveData()
    }

    val chats = Transformations.switchMap(query) {
        chatRepository.searchChat(it)
            .map { messages ->
                chatRepository.getChatById(messages.map { message ->
                    message.chatId
                })
            }.asLiveData()
    }

    override fun getLastMessage(chatId: String) = chatRepository.getLastMessage(chatId).asLiveData()

    override fun getProfilePicture(userId: String) = authenticationRepository.getUserPicture(userId).asLiveData()

    override fun getUsername(userId: String) = authenticationRepository.getUsername(userId).asLiveData()

//    fun getChatById(chatId: String) = viewModelScope.launch {
//        chatRepository.getChatById(chatId)
//    }
}