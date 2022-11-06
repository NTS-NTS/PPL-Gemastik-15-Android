package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.AddDisabilitiesRequest
import com.raassh.gemastik15.api.request.LoginRequest
import com.raassh.gemastik15.utils.callApi
import com.raassh.gemastik15.api.request.RegisterRequest

class AuthenticationRepository(private val apiService: ApiService) {
    fun register(name: String, username: String, email: String, password: String) = callApi {
        val req = RegisterRequest(
            name=name,
            email=email,
            username=username,
            password=password
        )

        apiService.register(req).data
    }

    fun login(username: String, password: String) = callApi {
        val req = LoginRequest(
            username=username,
            password=password
        )

        apiService.login(req).data
    }

    fun setDisabilities(token: String, disabilities: List<String>) = callApi {
        val req = AddDisabilitiesRequest(
            disabilities=disabilities
        )

        apiService.setDisabilities("Bearer $token", req)
    }
}