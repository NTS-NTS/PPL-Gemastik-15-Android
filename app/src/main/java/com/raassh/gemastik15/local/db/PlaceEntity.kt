package com.raassh.gemastik15.local.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val image: String,
    val distance: Double,
    val facilities: String,
    val accessTime: Long,
) : Parcelable