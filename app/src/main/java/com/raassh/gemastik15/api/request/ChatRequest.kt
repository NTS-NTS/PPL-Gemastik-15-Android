package com.raassh.gemastik15.api.request

import com.google.gson.annotations.SerializedName

data class StartChatRequest(
    @field:SerializedName("users")
    val users: List<String>
)

data class SendChatRequest(
    @field:SerializedName("chat_id")
    val chatId: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: String
)