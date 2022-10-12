package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class ContributionRequest (
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("place_id")
    val placeId: String,

    @field:SerializedName("facility")
    val facility: String,

    @field:SerializedName("quality")
    val quality: Int,
)