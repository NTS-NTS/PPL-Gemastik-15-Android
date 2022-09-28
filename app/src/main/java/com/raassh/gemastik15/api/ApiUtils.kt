package com.raassh.gemastik15.api

import com.google.gson.Gson
import com.raassh.gemastik15.api.response.ErrorResponse
import okhttp3.ResponseBody

fun getErrorResponse(body: ResponseBody?): ErrorResponse = if (body != null) {
    Gson().fromJson(body.string(), ErrorResponse::class.java)
} else {
    ErrorResponse("Unknown error", true, "Unknown error")
}