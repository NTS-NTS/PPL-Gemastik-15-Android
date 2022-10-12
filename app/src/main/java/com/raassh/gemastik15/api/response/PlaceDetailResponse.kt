package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class PlaceDetailResponse(

	@field:SerializedName("data")
	val data: PlaceDetailData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class PlaceDetailData(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("distance")
	val distance: Double,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("facilities")
	val facilities: List<FacilitiesItem>,

	@field:SerializedName("photos")
	val photos: List<String>,

	@field:SerializedName("longitude")
	val longitude: Double
)

data class FacilitiesItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("is_trusted")
	val isTrusted: Boolean,

	@field:SerializedName("quality")
	val quality: Double
)
