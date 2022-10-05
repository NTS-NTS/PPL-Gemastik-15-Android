package com.raassh.gemastik15.api

import com.raassh.gemastik15.api.request.LoginRequest
import com.raassh.gemastik15.api.request.RegisterRequest
import com.raassh.gemastik15.api.response.TokenResponse
import com.raassh.gemastik15.api.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequest
    ): UserResponse

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): TokenResponse
}