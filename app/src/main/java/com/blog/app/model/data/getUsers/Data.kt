package com.blog.app.model.data.getUsers

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("last_name") val last_name: String
)