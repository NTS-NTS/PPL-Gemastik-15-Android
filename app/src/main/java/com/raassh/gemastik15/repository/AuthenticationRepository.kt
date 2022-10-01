package com.raassh.gemastik15.repository

import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.getErrorResponse
import com.raassh.gemastik15.api.request.RegisterRequest
import com.raassh.gemastik15.utils.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class AuthenticationRepository(val apiService: ApiService) {
    fun register(name: String, email: String, password: String) = flow {
        emit(Resource.Loading())

        try {
            val req = RegisterRequest(
                name=name,
                email=email,
                password=password
            )
            val response = apiService.register(req)
            emit(Resource.Success(response.data))
        } catch (e: HttpException) {
            val error = getErrorResponse(e.response()?.errorBody())
            emit(Resource.Error(error.message, error))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        }
    }
}