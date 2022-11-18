package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class StartChatResponse(
    @field:SerializedName("data")
    val data: StartChatResponseData,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class StartChatResponseData(
    @field:SerializedName("chat_id")
    val chatId: String,

    @field:SerializedName("users")
    val users: List<String>,

    @field:SerializedName("messages")
    val messages: List<SendChatResponseData>
)

data class SendChatResponse(
    @field:SerializedName("data")
    val data: SendChatResponseData,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class SendChatResponseData(
    @field:SerializedName("chat_id")
    val chatId: String,

    @field:SerializedName("sender")
    val sender: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("timestamp")
    val timestamp: String,
)