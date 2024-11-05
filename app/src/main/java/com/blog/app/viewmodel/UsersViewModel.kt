package com.blog.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blog.app.model.data.ErrorResponse
import com.blog.app.model.data.auth.AuthRequest
import com.blog.app.model.data.auth.LoginResponse
import com.blog.app.model.data.auth.RegisterResponse
import com.blog.app.model.data.getUsers.UsersData
import com.blog.app.model.handleResponse.Event
import com.blog.app.model.repository.MainRepository
import com.google.gson.Gson
import com.blog.app.model.handleResponse.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UsersViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: MainRepository = MainRepository(application)

    private val _getUsersResponse = MutableLiveData<Event<Resource<UsersData>>>()
    val getUsersResponse: LiveData<Event<Resource<UsersData>>> = _getUsersResponse

    private val _loginResponse = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResponse: LiveData<Event<Resource<LoginResponse>>> = _loginResponse

    private val _registerResponse = MutableLiveData<Event<Resource<RegisterResponse>>>()
    val registerResponse: LiveData<Event<Resource<RegisterResponse>>> = _registerResponse

    fun getUsersList() = viewModelScope.launch {
        _getUsersResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.getUsersList()
            _getUsersResponse.postValue(handleResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getUsersResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _getUsersResponse.postValue(
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

    fun loginUser(authRequest: AuthRequest) = viewModelScope.launch {
        _loginResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.loginUser(authRequest)
            _loginResponse.postValue(handleLoginResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _loginResponse.postValue(
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

    fun registerUser(authRequest: AuthRequest) = viewModelScope.launch {
        _registerResponse.postValue(Event(Resource.Loading()))
        try {
            val response = appRepository.registerUser(authRequest)
            _registerResponse.postValue(handleRegisterResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _registerResponse.postValue(
                        Event(
                            Resource.Error(
                                "Request not succeed"
                            )
                        )
                    )
                }
                else -> {
                    _registerResponse.postValue(
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

    private fun handleLoginResponse(response: Response<LoginResponse>): Event<Resource<LoginResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.token.isNotEmpty()) {
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

    private fun handleRegisterResponse(response: Response<RegisterResponse>): Event<Resource<RegisterResponse>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.token.isNotEmpty()) {
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

    private fun handleResponse(response: Response<UsersData>): Event<Resource<UsersData>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return if (resultResponse.data.isNotEmpty()) {
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
