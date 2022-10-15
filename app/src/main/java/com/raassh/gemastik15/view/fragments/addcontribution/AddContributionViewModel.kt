package com.raassh.gemastik15.view.fragments.addcontribution

import androidx.lifecycle.*
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.local.db.Facility
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.repository.ContributionRepository
import com.raassh.gemastik15.repository.PlaceRepository
import kotlinx.coroutines.launch
import java.util.*

class AddContributionViewModel(
    private val contributionRepository: ContributionRepository,
    private val placeRepository: PlaceRepository,
) : ViewModel() {
    private val _currentFacility = MutableLiveData<Facility>()
    val currentFacility: MutableLiveData<Facility> = _currentFacility

    private val _isDone = MutableLiveData<Boolean>()
    val isDone: MutableLiveData<Boolean> = _isDone

    private val _facilities = MutableLiveData<List<Facility>>()
    val facilities: MutableLiveData<List<Facility>> = _facilities

    private val _index = MutableLiveData<Int>()
    private val index: MutableLiveData<Int> = _index

    private val query = MutableLiveData<PlaceDetailQuery>()

    val detail = Transformations.switchMap(query) {
        placeRepository.getPlaceDetail(it.id, it.lat, it.long).asLiveData()
    }

    fun getDetail(place: PlaceEntity, lat: Double, long: Double) {
        this.query.value = PlaceDetailQuery(place.id, lat, long)
    }

    fun nextFacility() {
        if (index.value!! < facilities.value!!.size - 1) {
            _index.value = index.value!! + 1
            _currentFacility.value = facilities.value!![index.value!!]
            _isDone.value = false
        } else {
            _isDone.value = true
        }
    }

    fun submitContribution(userId: String, placeId: String, quality: Int) =
        contributionRepository.addContribution(userId, placeId, currentFacility.value!!.name, quality).asLiveData()
}