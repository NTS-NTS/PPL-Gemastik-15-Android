package com.raassh.gemastik15.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String? = null, data: T? = null) : Resource<T>(data, message)

    private var isNotHandled = true

    fun getIfNotHandled() = if (isNotHandled) {
        isNotHandled = false
        this
    } else {
        null
    }
}
