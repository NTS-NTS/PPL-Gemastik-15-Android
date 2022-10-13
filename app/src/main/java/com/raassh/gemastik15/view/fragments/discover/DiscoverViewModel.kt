package com.raassh.gemastik15.view.fragments.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.repository.PlaceRepository

class DiscoverViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    val recentPlaces = placeRepository.getRecentPlaces().asLiveData()
}