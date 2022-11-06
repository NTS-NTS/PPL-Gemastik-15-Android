package com.raassh.gemastik15.view.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.local.preferences.SettingPreferences
import com.raassh.gemastik15.local.preferences.UserPreferences
import kotlinx.coroutines.launch

class MainActivityViewModel(private val userPref: UserPreferences, private val settingPref: SettingPreferences) : ViewModel() {
    val token = userPref.getToken().asLiveData()

    val theme = settingPref.getTheme().asLiveData()

    fun clearToken() {
        viewModelScope.launch {
            userPref.clearToken()
        }
    }
}