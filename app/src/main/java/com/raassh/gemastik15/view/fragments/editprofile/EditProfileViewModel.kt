package com.raassh.gemastik15.view.fragments.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class EditProfileViewModel(private val authenticationRepository: AuthenticationRepository) : ViewModel() {
    private var token = ""

    fun setToken(token: String) {
        this.token = token
    }

    fun getProfile() = authenticationRepository.getUserDetail(token).asLiveData()
}