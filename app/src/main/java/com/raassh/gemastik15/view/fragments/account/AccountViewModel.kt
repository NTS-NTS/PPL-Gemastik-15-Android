package com.raassh.gemastik15.view.fragments.account

import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.R
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.local.preferences.SettingPreferences
import com.raassh.gemastik15.local.preferences.UserPreferences
import kotlinx.coroutines.launch

class AccountViewModel(val userPreferences: UserPreferences, val settingPreferences: SettingPreferences) : ViewModel() {
    private val _arrayAdapter = MutableLiveData<ArrayAdapter<String>>()
    var arrayAdapter: MutableLiveData<ArrayAdapter<String>> = _arrayAdapter

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearToken()
        }
    }

    fun getToken() = userPreferences.getToken().asLiveData()

    fun setTheme(theme: String) = viewModelScope.launch {
        settingPreferences.setTheme(theme)
    }

    fun getTheme() = settingPreferences.getTheme().asLiveData()


}