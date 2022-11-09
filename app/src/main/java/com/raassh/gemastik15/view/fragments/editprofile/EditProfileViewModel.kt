package com.raassh.gemastik15.view.fragments.editprofile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.AuthenticationRepository

class EditProfileViewModel(private val authenticationRepository: AuthenticationRepository) :
    ViewModel() {
    private var token = ""
    private var selectedPicture: Bitmap? = null

    fun setToken(token: String) {
        this.token = token
    }

    fun setSelectedPicture(bitmap: Bitmap) {
        selectedPicture = bitmap
    }

    fun getProfile() = authenticationRepository.getUserDetail(token).asLiveData()

    fun editProfile(
        name: String? = null,
        username: String? = null,
        city: String? = null
    ) = authenticationRepository.editUserDetail(token, name, username, city, selectedPicture)
        .asLiveData()
}