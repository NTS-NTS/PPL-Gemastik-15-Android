package com.raassh.gemastik15.view.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.api.response.TokenData
import com.raassh.gemastik15.local.db.PlaceDatabase
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val authenticationRepository: AuthenticationRepository, private val pref: UserPreferences, private val placeDatabase: PlaceDatabase) : ViewModel() {
    fun login(username: String, password: String) =
        authenticationRepository.login(username, password).asLiveData()

    fun setUserData(tokenData: TokenData?) = viewModelScope.launch(Dispatchers.IO) {
        if (tokenData == null) {
            return@launch
        }

//        TODO: clear all tables not working as intended
        placeDatabase.clearAllTables()
        pref.setName(tokenData.name)
        pref.setUserName(tokenData.username)
        pref.setHasDisabilityTypes(tokenData.hasDisabilityTypes)
        pref.setIsVerified(tokenData.isVerified)
        pref.setIsModerator(tokenData.isModerator)
        pref.setIsBanned(tokenData.isBanned)
        pref.setToken(tokenData.token)
    }
}