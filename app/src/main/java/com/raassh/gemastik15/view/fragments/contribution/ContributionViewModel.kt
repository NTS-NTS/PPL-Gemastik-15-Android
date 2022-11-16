package com.raassh.gemastik15.view.fragments.contribution

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.api.response.ContributionUserData
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository

class ContributionViewModel(private val contributionRepository: ContributionRepository, private val placeRepository: PlaceRepository) : ViewModel() {
    private val token = MutableLiveData<String>()
    private val userId = MutableLiveData<String>()
    private var location = MutableLiveData<LatLng?>()
    val reviewedPlacesId = MutableLiveData<List<String>>()

    val contributionCount = Transformations.switchMap(token) {
        contributionRepository.getContributionCount(it).asLiveData()
    }

    fun setToken(token: String) {
        this.token.value = token
    }

    fun setUserId(userId: String) {
        this.userId.value = userId
    }

    fun setReviewedPlacesId(reviews: List<ContributionUserData>) {
        val placesId = reviews.map { it.place.id }
        reviewedPlacesId.value = placesId
    }

    val recent = placeRepository.getRecentPlaces().asLiveData()

    val nearby = Transformations.switchMap(location) {
        if (it == null) {
            return@switchMap null
        }

        placeRepository.searchPlaceNearby(it.latitude, it.longitude).asLiveData()
    }

    fun reviewHistory() = Transformations.switchMap(location) {
        if (it == null) {
            return@switchMap null
        }

        contributionRepository.getContributionsofUser(userId.value!!, it.latitude, it.longitude).asLiveData()
    }

    fun setLocation(location: LatLng?) {
        this.location.value = location
    }

    fun getDetail(place: PlaceEntity) =
        Transformations.switchMap(
            MutableLiveData(PlaceDetailQuery(place.id, location.value?.latitude, location.value?.longitude))
        ) {
            placeRepository.getPlaceDetail(it.id, it.lat, it.long).asLiveData()
        }

    fun deleteReview(placeId: String) = contributionRepository.deleteReview(token.value!!, placeId).asLiveData()
}