package com.blog.app.network

import android.content.Context
import com.blog.app.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClientofUser {

    fun create(context: Context): APIService {

        val httpClient = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                chain.proceed(requestBuilder.build())
            }

        val retrofit = Retrofit.Builder()
            .baseUrl(context.resources.getString(R.string.baseUrlUser))
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        return retrofit.create(APIService::class.java)
    }
}
