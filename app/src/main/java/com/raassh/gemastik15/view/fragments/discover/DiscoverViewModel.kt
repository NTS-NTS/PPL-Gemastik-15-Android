package com.raassh.gemastik15.view.fragments.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.local.preferences.UserPreferences
import com.raassh.gemastik15.repository.PlaceRepository
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.flow.map

class DiscoverViewModel(private val placeRepository: PlaceRepository, private val pref: UserPreferences)
    : ViewModel() {
    val recentPlaces = placeRepository.getRecentPlaces().asLiveData()

    val hasDisabilityTypes = pref.getHasDisabilityTypes().map {
        Resource.Success(it)
    }.asLiveData()
}