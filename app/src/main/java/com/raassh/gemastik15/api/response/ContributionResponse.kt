package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class ContributionResponse(
    @field:SerializedName("data")
    val data: ContributionData,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ContributionData(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("place_id")
    val place_id: String,

    @field:SerializedName("facility")
    val facility: String,

    @field:SerializedName("quality")
    val quality: Int
)

data class ContributionCountResponse(

    @field:SerializedName("data")
    val data: ContributionCountData,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ContributionCountData(

    @field:SerializedName("contribution count")
    val contributionCount: Int
)

data class ContributionsPlaceResponse(

    @field:SerializedName("data")
    val data: List<ReviewData>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ReviewData(

    @field:SerializedName("user")
    val user: UserData,

    @field:SerializedName("place_id")
    val place_id: String,

    @field:SerializedName("facilities")
    val facilities: List<FacilityQualityItem>,

    @field:SerializedName("review")
    val review: String?
)

data class FacilityQualityItem(

    @field:SerializedName("facility")
    val facility: String,

    @field:SerializedName("quality")
    val quality: Int,
)
