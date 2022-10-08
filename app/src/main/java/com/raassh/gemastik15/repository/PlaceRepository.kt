package com.raassh.gemastik15.repository

import android.util.Log
import com.raassh.gemastik15.local.db.PlaceDao
import com.raassh.gemastik15.local.db.PlaceEntity
import java.util.Calendar

class PlaceRepository(private val placeDao: PlaceDao) {
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
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis,
                ),
                PlaceEntity(
                    id = "2",
                    name = "Instiut Teknologi Sepuluh Nopember 1",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 1,
                ),
                PlaceEntity(
                    id = "3",
                    name = "Instiut Teknologi Sepuluh Nopember 2",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 2,
                ),
                PlaceEntity(
                    id = "4",
                    name = "Instiut Teknologi Sepuluh Nopember 3",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 3,
                ),
                PlaceEntity(
                    id = "5",
                    name = "Instiut Teknologi Sepuluh Nopember 4",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 4,
                ),
                PlaceEntity(
                    id = "6",
                    name = "Instiut Teknologi Sepuluh Nopember 5",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 5,
                ),
                PlaceEntity(
                    id = "7",
                    name = "Instiut Teknologi Sepuluh Nopember 6",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 6,
                ),
                PlaceEntity(
                    id = "8",
                    name = "Instiut Teknologi Sepuluh Nopember 7",
                    type = "Universitas",
                    image = "https://akcdn.detik.net.id/community/media/visual/2020/11/20/ilustrasi-its-institut-teknologi-sepuluh-nopember.jpeg?w=700&q=90",
                    distance = 0.1,
                    facilities = "Toilet,Lift,Ramp",
                    accessTime = Calendar.getInstance().timeInMillis + 7,
                )
            )
        )
        Log.d("TAG", "test: insert selesai")
    }
}