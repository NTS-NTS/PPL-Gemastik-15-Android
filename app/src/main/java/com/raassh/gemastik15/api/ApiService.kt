package com.raassh.gemastik15.api

import com.raassh.gemastik15.api.request.AddDisabilitiesRequest
import com.raassh.gemastik15.api.request.ContributionRequest
import com.raassh.gemastik15.api.request.LoginRequest
import com.raassh.gemastik15.api.request.RegisterRequest
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
}