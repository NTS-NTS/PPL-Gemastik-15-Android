package com.raassh.gemastik15.utils

import com.raassh.gemastik15.api.response.PlacesItem
import com.raassh.gemastik15.local.db.PlaceEntity
import java.util.*

fun placeItemToEntity(placesItem: PlacesItem): PlaceEntity {
    return PlaceEntity(
        placesItem.id,
        placesItem.name,
        placesItem.kind,
        placesItem.image,
        placesItem.distance,
        placesItem.facilities.joinToString(",") {
            it.name
        },
        Calendar.getInstance().timeInMillis
    )
}