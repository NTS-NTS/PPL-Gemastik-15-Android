package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("password")
	val password: String,
)
