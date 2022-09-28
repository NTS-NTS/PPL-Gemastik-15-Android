package com.raassh.gemastik15.view.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raassh.gemastik15.api.ApiService
import com.raassh.gemastik15.api.getErrorResponse
import com.raassh.gemastik15.api.request.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(val apiService: ApiService) : ViewModel() {
    fun register(name: String, email: String, password: String) {
        val req = RegisterRequest(name, email, password)

        viewModelScope.launch {
            try {
                apiService.register(req)
            }  catch (httpException: HttpException) {
                val error = getErrorResponse(httpException.response()?.errorBody())
                Log.e("TAG", "register: $error")
            } catch (e: Throwable) {
                Log.e("TAG", "register: ${e.message}")
            }
        }
    }
}