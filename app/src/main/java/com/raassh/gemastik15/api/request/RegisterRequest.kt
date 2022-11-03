package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String,
)