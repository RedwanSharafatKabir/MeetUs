package com.blog.app.network

import com.blog.app.model.data.auth.AuthRequest
import com.blog.app.model.data.auth.LoginResponse
import com.blog.app.model.data.auth.RegisterResponse
import com.blog.app.model.data.createPost.CreatePostRequest
import com.blog.app.model.data.getPost.PostsDataResponse
import com.blog.app.model.data.getPost.PostsDataResponseItem
import com.blog.app.model.data.getUsers.UsersData
import com.blog.app.model.handleResponse.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {

    @GET(EndPoints.USERS)
    suspend fun getUsersList(): Response<UsersData>

    @GET(EndPoints.POSTS)
    suspend fun getPostsList(): Response<PostsDataResponse>

    @POST(EndPoints.POSTS)
    suspend fun createPost(
        @Body createPostRequest: CreatePostRequest
    ): Response<PostsDataResponseItem>

    @POST(EndPoints.LOGIN)
    suspend fun loginUser(
        @Body authRequest: AuthRequest
    ): Response<LoginResponse>

    @POST(EndPoints.REGISTER)
    suspend fun registerUser(
        @Body authRequest: AuthRequest
    ): Response<RegisterResponse>
}
