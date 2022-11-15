package com.raassh.gemastik15.api

import com.raassh.gemastik15.api.request.*
import com.raassh.gemastik15.api.response.*
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): UserResponse

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): TokenResponse

    @GET("places/detail/{id}")
    suspend fun getPlaceDetail(
        @Path("id") id: String,
        @Query("lat") lat: Double?,
        @Query("long") long: Double?
    ): PlaceDetailResponse

    @GET("places/search")
    suspend fun searchPlaceByName(
        @Query("name") name: String,
        @Query("lat") lat: Double?,
        @Query("long") long: Double?,
    ): PlaceSearchResponse

    @GET("places/search/facilities")
    suspend fun searchPlaceByFacility(
        @Query("facility") facilities: List<String>,
        @Query("lat") lat: Double?,
        @Query("long") long: Double?,
    ): PlaceSearchResponse

    @GET("places/nearby")
    suspend fun searchPlaceNearby(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
    ): PlaceSearchResponse

//    @POST("contributions/")
//    suspend fun addContribution(
//        @Header("Authorization") authorization: String,
//        @Body body: ContributionRequest
//    ): ContributionResponse

    @POST("contributions/review")
    suspend fun addReview(
        @Header("Authorization") authorization: String,
        @Body body: ReviewRequest
    ): ReviewResponse

    @PUT("contributions/change")
    suspend fun changeReview(
        @Header("Authorization") authorization: String,
        @Body body: ReviewRequest
    ): ReviewResponse

    @GET("contributions/count")
    suspend fun getContributionCount(
        @Header("Authorization") authorization: String
    ): ContributionCountResponse

    @GET("contributions/{place_id}")
    suspend fun getContributionsofPlace(
        @Path("place_id") placeId: String
    ): ContributionsPlaceResponse

    @GET("contributions/{place_id}/users/{user_id}")
    suspend fun getContributionsofPlacebyUser(
        @Path("place_id") placeId: String,
        @Path("user_id") userId: String
    ): ContributionUserPlaceResponse

    @POST("auth/disabilities")
    suspend fun setDisabilities(
        @Header("Authorization") authorization: String,
        @Body body: AddDisabilitiesRequest
    ): GeneralResponse

    @POST("auth/password/change")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body body: ChangePasswordRequest
    ): GeneralResponse

    @GET("auth/detail")
    suspend fun getUserDetail(
        @Header("Authorization") authorization: String
    ): ProfileResponse

    @POST("auth/detail")
    suspend fun editUserDetail(
        @Header("Authorization") authorization: String,
        @Body body: EditProfileRequest
    ): ProfileResponse

    @POST("auth/verify/resend")
    suspend fun resendVerification(
        @Body body: ResendVerificationRequest
    ): GeneralResponse

    @GET("readings/news")
    suspend fun getNews(
        @Query("limit") limit: Int
    ): ArticleResponse

    @GET("readings/articles")
    suspend fun getArticles(
        @Query("limit") limit: Int
    ): ArticleResponse

    @POST("reports/contributions")
    suspend fun reportContribution(
        @Header("Authorization") authorization: String,
        @Body body: ReportContributionRequest
    ): GeneralResponse

    @GET("reports/contributions")
    suspend fun getContributionReports(
        @Header("Authorization") authorization: String
    ): ListReportContributionResponse

    @GET("reports/contributions/details/{place_id}/users/{user_id}")
    suspend fun getContributionReportDetails(
        @Header("Authorization") authorization: String,
        @Path("place_id") placeId: String,
        @Path("user_id") userId: String
    ): DetailReportContributionResponse

    @POST("reports/contributions/dismiss")
    suspend fun dismissContributionReport(
        @Header("Authorization") authorization: String,
        @Body body: DismissContributionReportRequest
    ): GeneralResponse

    @POST("reports/contributions/moderate")
    suspend fun moderateContributionReport(
        @Header("Authorization") authorization: String,
        @Body body: ReportContributionRequest
    ): GeneralResponse

    @POST("reports/users")
    suspend fun reportUser(
        @Header("Authorization") authorization: String,
        @Body body: ReportUserRequest
    ): GeneralResponse

    @GET("reports/users")
    suspend fun getUserReports(
        @Header("Authorization") authorization: String
    ): ListReportUserResponse

    @GET("reports/users/details/{user_id}")
    suspend fun getUserReportDetails(
        @Header("Authorization") authorization: String,
        @Path("user_id") userId: String
    ): DetailReportUserResponse

    @POST("reports/users/dismiss")
    suspend fun dismissUserReport(
        @Header("Authorization") authorization: String,
        @Body body: DismissUserReportRequest
    ): GeneralResponse

    @POST("reports/users/moderate")
    suspend fun moderateUserReport(
        @Header("Authorization") authorization: String,
        @Body body: ReportUserRequest
    ): GeneralResponse
}