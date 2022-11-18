package com.raassh.gemastik15.repository

import android.graphics.Bitmap
import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.request.*
import com.raassh.gemastik15.utils.callApi
import com.raassh.gemastik15.utils.toBase64

class AuthenticationRepository(private val apiService: ApiService) {
    fun register(name: String, username: String, email: String, password: String) = callApi {
        val req = RegisterRequest(
            name = name,
            email = email,
            username = username,
            password = password
        )

        apiService.register(req).data
    }

    fun login(username: String, password: String) = callApi {
        val req = LoginRequest(
            username = username,
            password = password
        )

        apiService.login(req).data
    }

    suspend fun logout(token: String) {
        val req = LogoutRequest(
            token = token
        )

        apiService.logout(req)
    }

    fun setDisabilities(token: String, disabilities: List<String>) = callApi {
        val req = AddDisabilitiesRequest(
            disabilities = disabilities
        )

        apiService.setDisabilities("Bearer $token", req)
    }

    fun changePassword(token: String, oldPassword: String, newPassword: String) = callApi {
        val req = ChangePasswordRequest(
            oldPassword = oldPassword,
            newPassword = newPassword
        )

        apiService.changePassword("Bearer $token", req)
    }

    fun getUserDetail(token: String) = callApi {
        apiService.getUserDetail("Bearer $token").data
    }

    fun editUserDetail(
        token: String,
        name: String? = null,
        username: String? = null,
        city: String? = null,
        profilePicture: Bitmap? = null
    ) = callApi {
        val req = EditProfileRequest(
            name = name,
            username = username,
            city = city,
            profilePicture = if (profilePicture != null) "jpeg;${profilePicture.toBase64()}" else null
        )

        apiService.editUserDetail("Bearer $token", req).data
    }

    fun resendVerification(email: String) = callApi {
        val req = ResendVerificationRequest(
            email = email
        )

        apiService.resendVerification(req)
    }

    fun searchUser(username: String) = callApi {
        apiService.searchUserByUsername(username).data
    }
}