package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class ResendVerificationRequest(
    @field:SerializedName("email")
    val email: String
)
