package com.raassh.gemastik15.view.fragments.discover

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.PlaceRepository
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.flow.map

class DiscoverViewModel(private val placeRepository: PlaceRepository, private val pref: UserPreferences)
    : ViewModel() {

    private val token = MutableLiveData<String>()
    private var location = MutableLiveData<LatLng?>()

    fun setToken(token: String) {
        this.token.value = token
    }

    fun setLocation(location: LatLng?) {
        this.location.value = location
    }

    val recentPlaces = placeRepository.getRecentPlaces().asLiveData()

    val hasDisabilityTypes = pref.getHasDisabilityTypes().map {
        Resource.Success(it)
    }.asLiveData()

    fun getFavoritePlaces() = placeRepository.getFavoritePlaces(token.value!!).asLiveData()

    fun recommendedPlaces() = Transformations.switchMap(location) {
        if (it == null) {
            return@switchMap null
        }
        Log.d("DiscoverViewModel", "recommendedPlaces: ${it.latitude}, ${it.longitude}")
        placeRepository.getRecommendedPlaces(token.value!!, it.latitude, it.longitude).asLiveData()
    }
}