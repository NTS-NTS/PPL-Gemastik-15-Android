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
