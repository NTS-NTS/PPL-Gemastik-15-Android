package com.raassh.gemastik15.view.fragments.editprofile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.api.response.TokenData
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val pref: UserPreferences
) :
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

    fun setUserData(name: String, username: String) = viewModelScope.launch(Dispatchers.IO) {
        pref.setName(name)
        pref.setUserName(username)
    }
}