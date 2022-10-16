package com.raassh.gemastik15.view.fragments.contribution

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository

class ContributionViewModel(private val contributionRepository: ContributionRepository, private val placeRepository: PlaceRepository) : ViewModel() {
    private val userId = MutableLiveData<String>()
    private var location = MutableLiveData<LatLng?>()

    val contributionCount = Transformations.switchMap(userId) {
        contributionRepository.getContributionCount(it).asLiveData()
    }

    fun setUserId(userId: String) {
        this.userId.value = userId
    }

    val recent = placeRepository.getRecentPlaces().asLiveData()

    val nearby = Transformations.switchMap(location) {
        placeRepository.searchPlaceByName("", it?.latitude, it?.longitude).asLiveData()
    }

    fun setLocation(location: LatLng?) {
        this.location.value = location
    }
}