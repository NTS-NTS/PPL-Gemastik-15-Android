package com.raassh.gemastik15.view.fragments.contribution

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.api.response.PlaceDetailData
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

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

    fun getDetail(place: PlaceEntity) =
        Transformations.switchMap(
            MutableLiveData(PlaceDetailQuery(place.id, location.value?.latitude, location.value?.longitude))
        ) {
            placeRepository.getPlaceDetail(it.id, it.lat, it.long).asLiveData()
        }

}