package com.blog.app.model.data.createPost

import com.google.gson.annotations.SerializedName

data class CreatePostRequest (
    @SerializedName("body") val body: String,
    @SerializedName("title") val title: String,
    @SerializedName("userId") val userId: Int
)