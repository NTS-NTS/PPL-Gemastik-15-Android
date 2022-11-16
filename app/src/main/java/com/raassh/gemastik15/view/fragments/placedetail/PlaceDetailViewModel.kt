package com.raassh.gemastik15.view.fragments.placedetail

import androidx.lifecycle.*
import com.auth0.android.jwt.JWT
import com.raassh.gemastik15.R
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.api.response.ContributionUserPlaceData
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository
import com.raassh.gemastik15.utils.Resource
import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import kotlinx.coroutines.launch
import java.util.*

class PlaceDetailViewModel(
    private val placeRepository: PlaceRepository,
    private val contributionRepository: ContributionRepository
    ) : ViewModel() {
    private val token = MutableLiveData<String>()

    fun setToken(token: String) {
        this.token.value = token
    }

    private val query = MutableLiveData<PlaceDetailQuery>()
    val userId = MutableLiveData<String>()

    fun setUserId(userId: String) {
        this.userId.value = userId
    }

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

    fun getUserReview(placeId: String, userId: String) : LiveData<Resource<ContributionUserPlaceData>> {
        return contributionRepository.getContributionsofPlacebyUser(placeId, userId).asLiveData()
    }

    fun deleteReview(placeId: String) = contributionRepository.deleteReview(token.value!!, placeId).asLiveData()
}