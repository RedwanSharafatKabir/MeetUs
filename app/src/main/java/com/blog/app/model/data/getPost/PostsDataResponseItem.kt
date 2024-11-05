package com.blog.app.model.data.getPost

import com.google.gson.annotations.SerializedName

data class PostsDataResponseItem(
    @SerializedName("body") val body: String,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("userId") val userId: Int
)