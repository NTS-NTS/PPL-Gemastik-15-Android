package com.raassh.gemastik15.view.fragments.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class RegisterViewModel(val authenticationRepository: AuthenticationRepository) : ViewModel() {
    fun register(name: String, username: String, email: String, password: String) =
        authenticationRepository.register(name, username, email, password).asLiveData()
}