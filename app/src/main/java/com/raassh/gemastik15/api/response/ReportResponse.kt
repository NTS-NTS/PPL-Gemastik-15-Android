package com.raassh.gemastik15.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListReportContributionResponse(
    @field:SerializedName("data")
    val data: DataReportContributionResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class DataReportContributionResponse(
    @field:SerializedName("contributions")
    val contributions: List<ItemReportContributionResponse>
)

@Parcelize
data class ItemReportContributionResponse(
    @field:SerializedName("place_id")
    val placeId: String,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("place")
    val place: String,

    @field:SerializedName("user")
    val user: String,

    @field:SerializedName("report_count")
    val reportCount: Int
) : Parcelable

data class DetailReportContributionResponse(
    @field:SerializedName("data")
    val data: DataDetailReportContributionResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class DataDetailReportContributionResponse(
    @field:SerializedName("review")
    val review: String,

    @field:SerializedName("report_count")
    val reportCount: Int,

    @field:SerializedName("report_reason")
    val reportReason: List<String>,

    @field:SerializedName("facilities")
    val facilities: List<ReportFacilitiesItem>,
)

data class ReportFacilitiesItem(
    @field:SerializedName("facility")
    val name: String,

    @field:SerializedName("quality")
    val quality: Double
)

data class ListReportUserResponse(
    @field:SerializedName("data")
    val data: DataReportUserResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class DataReportUserResponse(
    @field:SerializedName("users")
    val users: List<ItemReportUserResponse>
)

@Parcelize
data class ItemReportUserResponse(
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("report_count")
    val reportCount: Int
) : Parcelable

data class DetailReportUserResponse(
    @field:SerializedName("data")
    val data: DataDetailReportUserResponse,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class DataDetailReportUserResponse(
    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("report_count")
    val reportCount: Int,

    @field:SerializedName("report_reason")
    val reportReason: List<String>,
)