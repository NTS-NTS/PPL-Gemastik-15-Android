package com.raassh.gemastik15.repository

import android.util.Log
import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.local.db.PlaceDao
import com.raassh.gemastik15.local.db.PlaceEntity
import com.raassh.gemastik15.utils.callApi
import java.util.Calendar

class PlaceRepository(private val apiService: ApiService, private val placeDao: PlaceDao) {
    fun getPlaceDetail(id: String, lat: Double, long: Double) = callApi {
        apiService.getPlaceDetail(id, lat, long).data
    }

    fun searchPlaceByName(name: String, lat: Double, long: Double) = callApi {
        Log.d("TAG", "searchPlaceWithFacility: $name")
        apiService.searchPlaceByName(name, lat, long).data
    }

    fun searchPlaceByFacility(facilities: List<String>, lat: Double, long: Double) = callApi {
        apiService.searchPlaceByFacility(facilities, lat, long).data
    }

    fun getRecentPlaces() = placeDao.getRecentPlaces()

    suspend fun insertPlacesToDB(places: List<PlaceEntity>) = placeDao.insertPlaces(places)

    suspend fun test() {
        Log.d("TAG", "test: insert mulai")
        insertPlacesToDB(
            listOf(
                PlaceEntity(
                    id = "1",
                    name = "Instiut Teknologi Sepuluh Nopember 0",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "",
                    latitude = -7.282,
                    longitude = 112.794,
                    accessTime = Calendar.getInstance().timeInMillis,
                ),
                PlaceEntity(
                    id = "2",
                    name = "Instiut Teknologi Sepuluh Nopember 1",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    latitude = -7.282,
                    longitude = 112.794,
                    accessTime = Calendar.getInstance().timeInMillis + 1,
                ),
                PlaceEntity(
                    id = "3",
                    name = "Instiut Teknologi Sepuluh Nopember 2",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    latitude = -7.282,
                    longitude = 112.794,
                    accessTime = Calendar.getInstance().timeInMillis + 2,
                )
            )
        )
        Log.d("TAG", "test: insert selesai")
    }
}