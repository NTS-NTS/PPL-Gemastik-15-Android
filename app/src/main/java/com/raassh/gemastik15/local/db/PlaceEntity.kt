package com.raassh.gemastik15.local.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceEntity(
    val id: String,
    val name: String,
    val type: String,
    val image: String,
    val distance: Double,
    val facilities: String
) : Parcelable