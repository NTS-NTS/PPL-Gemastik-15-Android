package com.raassh.gemastik15.view.fragments.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.repository.PlaceRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    val recentPlaces = placeRepository.getRecentPlaces().asLiveData()

    init {
        viewModelScope.launch {
            placeRepository.test()
        }
    }
}