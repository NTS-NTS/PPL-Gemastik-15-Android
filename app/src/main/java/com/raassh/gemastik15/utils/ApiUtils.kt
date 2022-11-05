package com.raassh.gemastik15.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.raassh.gemastik15.api.response.ErrorResponse
import com.raassh.gemastik15.view.activity.main.MainActivity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException

fun getErrorResponse(body: ResponseBody?): ErrorResponse = if (body != null) {
    Gson().fromJson(body.string(), ErrorResponse::class.java)
} else {
    ErrorResponse("Unknown error", true, "Unknown error")
}

inline fun <T> callApi(crossinline apiCall: suspend () -> T) = flow<Resource<T>> {
    emit(Resource.Loading(null))

    val data = apiCall()
    emit(Resource.Success(data))
}.catch { e ->
    if (e is HttpException) {
        val error = getErrorResponse(e.response()?.errorBody())
        emit(Resource.Error(error.data, null))
        Log.e("CallApi", error.data, e)
    } else {
        emit(Resource.Error(e.message, null))
        Log.e("CallApi", e.message, e)
    }
}

// Not the ideal way to check error, but it'll do for now
fun Context.checkAuthError(errorMessage: String?) {
    if (errorMessage != "Invalid token")
        return

    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        putExtra(MainActivity.NEED_RELOGIN, true)
    }
    startActivity(intent)
}