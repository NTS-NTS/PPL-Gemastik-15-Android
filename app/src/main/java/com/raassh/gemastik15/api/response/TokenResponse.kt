package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("data")
	val data: TokenData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,
)

data class TokenData(

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("has_disability_types")
	val hasDisabilityTypes: Boolean,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("is_moderator")
	val isModerator: Boolean,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("is_verified")
	val isVerified: Boolean,
)
