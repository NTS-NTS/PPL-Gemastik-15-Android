package com.raassh.gemastik15.view.fragments.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.repository.PlaceRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    val recentPlaces = placeRepository.getRecentPlaces().asLiveData()

    // for testing only, delete later
    init {
        viewModelScope.launch {
            placeRepository.test()
            placeRepository.getPlaceDetail("63403ccf3446f21468402ee1", 2.3, 1.2)
            placeRepository.searchPlaceByName("nama", 2.3, 1.2)
            placeRepository.searchPlaceByFacility(listOf("fasilitas1", "fasilitas2"), 2.3, 1.2)
        }
    }
}