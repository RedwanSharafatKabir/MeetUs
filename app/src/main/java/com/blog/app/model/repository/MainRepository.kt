package com.blog.app.model.repository

import android.content.Context
import com.blog.app.model.data.auth.AuthRequest
import com.blog.app.model.data.createPost.CreatePostRequest
import com.blog.app.network.APIClientOfPosts
import com.blog.app.network.APIClientofUser
import com.blog.app.network.APIService
import retrofit2.http.Body

class MainRepository(ctx: Context) {

    private val usersApiService: APIService = APIClientofUser.create(ctx)
    private val postsApiService: APIService = APIClientOfPosts.create(ctx)

    suspend fun getUsersList() = usersApiService.getUsersList()

    suspend fun loginUser(@Body authRequest: AuthRequest) = usersApiService.loginUser(authRequest)

    suspend fun registerUser(@Body authRequest: AuthRequest) = usersApiService.registerUser(authRequest)

    suspend fun getPostsList() = postsApiService.getPostsList()

    suspend fun createPost(@Body createPostRequest: CreatePostRequest) = postsApiService.createPost(createPostRequest)
}
