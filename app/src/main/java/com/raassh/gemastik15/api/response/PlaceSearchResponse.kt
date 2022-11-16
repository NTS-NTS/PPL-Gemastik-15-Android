package com.raassh.gemastik15.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PlaceSearchResponse(

	@field:SerializedName("data")
	val data: List<PlacesItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class PlacesItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("distance")
	val distance: Double,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("longitude")
	val longitude: Double,

	@field:SerializedName("facilities")
	val facilities: List<FacilitiesItem>
) : Parcelable
