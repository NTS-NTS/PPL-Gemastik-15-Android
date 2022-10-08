package com.raassh.gemastik15.view.activity.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class DashboardViewModel : ViewModel() {
    private val _location = MutableLiveData<LatLng>()
    val location: LiveData<LatLng> = _location

    fun setLocation(lat: Double, lng: Double) {
        _location.value = LatLng(lat, lng)
    }
}