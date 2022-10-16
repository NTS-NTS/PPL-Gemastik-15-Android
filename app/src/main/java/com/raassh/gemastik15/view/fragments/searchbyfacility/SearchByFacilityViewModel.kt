package com.raassh.gemastik15.view.fragments.searchbyfacility

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.api.request.SearchByFacilitiesQuery
import com.raassh.gemastik15.repository.PlaceRepository

class SearchByFacilityViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    private var query = MutableLiveData<SearchByFacilitiesQuery>()

    val places = Transformations.switchMap(query) {
        placeRepository.searchPlaceByFacility(it.facilities, it.lat, it.long).asLiveData()
    }

    fun searchPlace(facilities: List<String>, lat: Double?, long: Double?) {
        this.query.value = SearchByFacilitiesQuery(facilities, lat, long)
    }
}