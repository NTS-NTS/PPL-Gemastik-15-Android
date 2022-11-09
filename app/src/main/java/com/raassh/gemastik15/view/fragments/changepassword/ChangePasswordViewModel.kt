package com.raassh.gemastik15.view.fragments.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class ChangePasswordViewModel(private val authenticationRepository: AuthenticationRepository) : ViewModel() {
    private var token = ""

    fun setToken(token: String) {
        this.token = token
    }

    fun changePassword(oldPassword: String, newPassword: String) = authenticationRepository.changePassword(token, oldPassword, newPassword).asLiveData()
}