package com.raassh.gemastik15.view.fragments.searchresult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raassh.gemastik15.api.request.SearchByNameQuery
import com.raassh.gemastik15.repository.PlaceRepository

class SearchResultViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    private var query = MutableLiveData<SearchByNameQuery>()

    val places = Transformations.switchMap(query) {
        placeRepository.searchPlaceByName(it.name, it.lat, it.long).asLiveData()
    }

    fun searchPlace(query: String, lat: Double?, long: Double?) {
        this.query.value = SearchByNameQuery(query, lat, long)
    }
}