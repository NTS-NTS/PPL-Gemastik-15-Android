package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("data")
	val data: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
