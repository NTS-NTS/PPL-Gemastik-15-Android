package com.raassh.gemastik15.api

import com.raassh.gemastik15.api.request.LoginRequest
import com.raassh.gemastik15.api.request.RegisterRequest
import com.raassh.gemastik15.api.response.PlaceDetailResponse
import com.raassh.gemastik15.api.response.PlaceSearchResponse
import com.raassh.gemastik15.api.response.TokenResponse
import com.raassh.gemastik15.api.response.UserResponse
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
    suspend fun searchPlace(
        @Query("name") name: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double,
    ): PlaceSearchResponse

    @GET("places/search/facilities")
    suspend fun searchPlaceWithFacility(
        @Query("facility") facilities: List<String>,
        @Query("lat") lat: Double,
        @Query("long") long: Double,
    ): PlaceSearchResponse
}