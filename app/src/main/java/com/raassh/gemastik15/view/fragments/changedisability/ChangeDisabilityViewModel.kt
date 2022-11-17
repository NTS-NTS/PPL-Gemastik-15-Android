package com.raassh.gemastik15.view.fragments.changedisability

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.api.response.GeneralResponse
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChangeDisabilityViewModel(private val authenticationRepository: AuthenticationRepository, private val userPreferences: UserPreferences) : ViewModel() {
    fun setDisabilityTypes(disabilityTypes: List<String>): LiveData<Resource<GeneralResponse>> {
        var token = ""

        viewModelScope.launch {
            token = userPreferences.getToken().first() ?: ""
        }

        return authenticationRepository.setDisabilities(token, disabilityTypes).asLiveData()
    }
}