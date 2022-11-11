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

    @POST("contributions/")
    suspend fun addContribution(
        @Header("Authorization") authorization: String,
        @Body body: ContributionRequest
    ): ContributionResponse

    @POST("contributions/change")
    suspend fun changeContribution(
        @Header("Authorization") authorization: String,
        @Body body: ContributionRequest
    ): ContributionResponse

    @GET("contributions/count")
    suspend fun getContributionCount(
        @Header("Authorization") authorization: String
    ): ContributionCountResponse

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
}