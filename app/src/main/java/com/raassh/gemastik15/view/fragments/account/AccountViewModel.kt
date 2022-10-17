package com.raassh.gemastik15.view.fragments.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.local.preferences.UserPreferences
import kotlinx.coroutines.launch

class AccountViewModel(val userPreferences: UserPreferences) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearToken()
        }
    }
}