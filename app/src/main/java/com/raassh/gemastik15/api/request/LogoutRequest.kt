package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
	@field:SerializedName("token")
	val token: String
)
