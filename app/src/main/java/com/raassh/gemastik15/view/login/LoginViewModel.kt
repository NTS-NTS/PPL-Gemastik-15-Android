package com.raassh.gemastik15.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class LoginViewModel(private val authenticationRepository: AuthenticationRepository) : ViewModel() {
    fun login(email: String, password: String) =
        authenticationRepository.login(email, password).asLiveData()
}