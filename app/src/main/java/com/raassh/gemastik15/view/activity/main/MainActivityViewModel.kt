package com.raassh.gemastik15.view.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.local.preferences.UserPreferences

class MainActivityViewModel(val pref: UserPreferences) : ViewModel() {
    fun getToken() = pref.getToken().asLiveData()
}