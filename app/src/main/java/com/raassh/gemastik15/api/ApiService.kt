package com.raassh.gemastik15.api

import com.raassh.gemastik15.api.request.ContributionRequest
import com.raassh.gemastik15.api.request.LoginRequest
import com.raassh.gemastik15.api.request.RegisterRequest
import com.raassh.gemastik15.api.response.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): PlaceDetailResponse

    @GET("places/search")
    suspend fun searchPlaceByName(
        @Query("name") name: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double,
    ): PlaceSearchResponse

    @GET("places/search/facilities")
    suspend fun searchPlaceByFacility(
        @Query("facility") facilities: List<String>,
        @Query("lat") lat: Double,
        @Query("long") long: Double,
    ): PlaceSearchResponse

    @POST("auth/contribution")
    suspend fun addContribution(
        @Body body: ContributionRequest
    ): ContributionResponse

    @POST("auth/contribution/change")
    suspend fun changeContribution(
        @Body body: ContributionRequest
    ): ContributionResponse
}