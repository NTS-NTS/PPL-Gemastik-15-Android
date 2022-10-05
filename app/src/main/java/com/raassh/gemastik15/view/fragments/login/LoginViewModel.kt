package com.raassh.gemastik15.view.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authenticationRepository: AuthenticationRepository, private val pref: UserPreferences) : ViewModel() {
    fun login(email: String, password: String) =
        authenticationRepository.login(email, password).asLiveData()

    fun setToken(token: String) = viewModelScope.launch {
        pref.setToken(token)
    }
}