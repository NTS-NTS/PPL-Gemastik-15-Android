package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @field:SerializedName("old_password")
    val oldPassword: String,

    @field:SerializedName("new_password")
    val newPassword: String
)