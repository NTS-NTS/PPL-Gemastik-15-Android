package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.local.db.PlaceDao
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.callApi

class PlaceRepository(private val apiService: ApiService, private val placeDao: PlaceDao) {
    fun getPlaceDetail(id: String, lat: Double?, long: Double?) = callApi {
        apiService.getPlaceDetail(id, lat, long).data
    }

    fun searchPlaceByName(name: String, lat: Double?, long: Double?) = callApi {
        apiService.searchPlaceByName(name, lat, long).data
    }

    fun searchPlaceByFacility(facilities: List<String>, lat: Double?, long: Double?) = callApi {
        apiService.searchPlaceByFacility(facilities, lat, long).data
    }

    fun getRecentPlaces() = placeDao.getRecentPlaces()

    suspend fun insertPlacesToDB(places: List<PlaceEntity>) = placeDao.insertPlaces(places)
}