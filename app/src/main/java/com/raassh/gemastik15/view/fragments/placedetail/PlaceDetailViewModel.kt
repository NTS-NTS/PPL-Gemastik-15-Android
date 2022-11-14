package com.raassh.gemastik15.view.fragments.placedetail

import androidx.lifecycle.*
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository
import kotlinx.coroutines.launch
import java.util.*

class PlaceDetailViewModel(
    private val placeRepository: PlaceRepository,
    private val contributionRepository: ContributionRepository
    ) : ViewModel() {
    private val query = MutableLiveData<PlaceDetailQuery>()

    val detail = Transformations.switchMap(query) {
        placeRepository.getPlaceDetail(it.id, it.lat, it.long).asLiveData()
    }

    fun getDetail(place: PlaceEntity, lat: Double?, long: Double?) {
        this.query.value = PlaceDetailQuery(place.id, lat, long)

        viewModelScope.launch {
            place.accessTime = Calendar.getInstance().timeInMillis
            placeRepository.insertPlacesToDB(listOf(place))
        }
    }

    fun getReviews(placeId: String) = contributionRepository.getContributionsofPlace(placeId).asLiveData()
}