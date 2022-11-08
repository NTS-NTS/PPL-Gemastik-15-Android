package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class EditProfileRequest(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_picture")
    val profilePicture: String? = null,
)
