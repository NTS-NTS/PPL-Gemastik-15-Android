package com.raassh.gemastik15.api.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

    @field:SerializedName("data")
    val data: List<Article>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class Article(

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("top_image_url")
    val imageUrl: String,

    @field:SerializedName("publish_date")
    val publishTime: String,

    @field:SerializedName("source")
    val source: String
)