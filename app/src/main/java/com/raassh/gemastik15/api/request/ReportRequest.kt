package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class ReportContributionRequest(
    @field:SerializedName("place_id")
    val placeId: String,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("reason")
    val reason: String,
)

data class DismissContributionReportRequest(
    @field:SerializedName("place_id")
    val placeId: String,

    @field:SerializedName("user_id")
    val userId: String,
)

data class ReportUserRequest(
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("reason")
    val reason: String,
)

data class DismissUserReportRequest(
    @field:SerializedName("user_id")
    val userId: String,
)