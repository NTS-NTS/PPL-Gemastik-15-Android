package com.raassh.gemastik15.view.fragments.account

import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.local.preferences.SettingPreferences
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.AuthenticationRepository
import kotlinx.coroutines.launch

class AccountViewModel(
    val userPreferences: UserPreferences,
    val settingPreferences: SettingPreferences,
    val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    private val _arrayAdapter = MutableLiveData<ArrayAdapter<String>>()
    var arrayAdapter: MutableLiveData<ArrayAdapter<String>> = _arrayAdapter

    private var token = ""

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout(token)
            userPreferences.clearToken()
        }
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun setTheme(theme: String) = viewModelScope.launch {
        settingPreferences.setTheme(theme)
    }

    fun getTheme() = settingPreferences.getTheme().asLiveData()

    fun resendVerification(email: String) =
        authenticationRepository.resendVerification(email).asLiveData()

    fun getProfile() = authenticationRepository.getUserDetail(token).asLiveData()
}