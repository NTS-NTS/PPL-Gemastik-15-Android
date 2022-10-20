package com.raassh.gemastik15.view.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.local.preferences.SettingPreferences
import com.raassh.gemastik15.local.preferences.UserPreferences

class MainActivityViewModel(private val userPref: UserPreferences, private val settingPref: SettingPreferences) : ViewModel() {
    fun getToken() = userPref.getToken().asLiveData()

    fun getTheme() = settingPref.getTheme().asLiveData()
}