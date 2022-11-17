package com.raassh.gemastik15.view.activity.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.local.preferences.UserPreferences

class DashboardViewModel(private val pref: UserPreferences) : ViewModel() {
    private val _location = MutableLiveData<LatLng?>(null)
    val location: LiveData<LatLng?> = _location

    fun setLocation(latLng: LatLng?) {
        _location.value = latLng
        Log.d("DashboardViewModel", "setLocation: ${location.value}")
    }

    fun getToken() = pref.getToken().asLiveData()

    fun getIsModerator() = pref.getIsModerator().asLiveData()

    fun getIsVerified() = pref.getIsVerified().asLiveData()
}