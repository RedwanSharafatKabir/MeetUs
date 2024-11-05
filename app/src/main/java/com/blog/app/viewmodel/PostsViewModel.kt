package com.blog.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blog.app.model.data.ErrorResponse
import com.blog.app.model.data.createPost.CreatePostRequest
import com.blog.app.model.data.getPost.PostsDataResponse
import com.blog.app.model.data.getPost.PostsDataResponseItem
import com.blog.app.model.handleResponse.Event
import com.blog.app.model.repository.MainRepository
import com.google.gson.Gson
import com.blog.app.model.handleResponse.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PostsViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: MainRepository = MainRepository(application)

    private val _getPostsResponse = MutableLiveData<Event<Resource<PostsDataResponse>>>()
    val getPostsResponse: LiveData<Event<Resource<PostsDataResponse>>> = _getPostsResponse

    private val _createPostsResponse = MutableLiveData<Event<Resource<PostsDataResponseItem>>>()
    val createPostsResponse: LiveData<Event<Resource<PostsDataResponseItem>>> = _createPostsResponse

    fun createPost(createPostRequest: CreatePostRequest) = viewModelScope.launch {
        _createPostsResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.createPost(createPostRequest)
            _createPostsResponse.postValue(handleCreatePostResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _createPostsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _createPostsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
            }
        }
    }

    fun getPostsList() = viewModelScope.launch {
        _getPostsResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.getPostsList()
            _getPostsResponse.postValue(handleResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getPostsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _getPostsResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleCreatePostResponse(response: Response<PostsDataResponseItem>): Event<Resource<PostsDataResponseItem>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.title.isNotEmpty()) {
                    Event(Resource.Success(resultResponse))

                } else {
                    Event(Resource.Error("Post could not create."))
                }
            }
        }

        return try {
            val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            Event(Resource.Error(errorBody.error))

        } catch (e: Exception) {
            Event(
                Resource.Error(
                    "Request not succeed"
                ))
        }
    }

    private fun handleResponse(response: Response<PostsDataResponse>): Event<Resource<PostsDataResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.isNotEmpty()) {
                    Event(Resource.Success(resultResponse))

                } else {
                    Event(Resource.Error("No post published today."))
                }
            }
        }

        return try {
            val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            Event(Resource.Error(errorBody.error))

        } catch (e: Exception) {
            Event(
                Resource.Error(
                    "Request not succeed"
                ))
        }
    }
}
