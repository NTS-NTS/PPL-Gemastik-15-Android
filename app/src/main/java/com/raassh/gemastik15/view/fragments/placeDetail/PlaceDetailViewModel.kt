package com.raassh.gemastik15.view.fragments.placeDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.api.request.PlaceDetailQuery
import com.raassh.gemastik15.repository.PlaceRepository

class PlaceDetailViewModel(placeRepository: PlaceRepository) : ViewModel() {
    private val query = MutableLiveData<PlaceDetailQuery>()

    val detail = Transformations.switchMap(query) {
        placeRepository.getPlaceDetail(it.id, it.lat, it.long).asLiveData()
    }

    fun setId(id: String, lat: Double, long: Double) {
        this.query.value = PlaceDetailQuery(id, lat, long)
    }
}