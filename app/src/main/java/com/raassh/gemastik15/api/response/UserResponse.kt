package com.raassh.gemastik15.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(

	@field:SerializedName("data")
	val data: UserData,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class UserData(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String
) : Parcelable


data class SearchUserResponse(

	@field:SerializedName("data")
	val data: List<SearchUserItem>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class SearchUserItem(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("profile_picture")
	val profilePicture: String
)
