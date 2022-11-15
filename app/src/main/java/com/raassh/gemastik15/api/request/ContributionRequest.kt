package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName
import com.raassh.gemastik15.api.response.FacilityQualityItem

//data class ContributionRequest (
//    @field:SerializedName("place_id")
//    val placeId: String,
//
//    @field:SerializedName("facility")
//    val facility: String,
//
//    @field:SerializedName("quality")
//    val quality: Int,
//)

data class ReviewRequest (
    @field:SerializedName("place_id")
    val placeId: String,

    @field:SerializedName("review")
    val review: String,

    @field:SerializedName("facility_reviews")
    val facilityReviews: List<FacilityQualityItem>
)