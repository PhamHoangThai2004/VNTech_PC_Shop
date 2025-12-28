package com.pht.vntechpc.data.remote.network

import android.content.Context
import com.pht.vntechpc.data.local.UserPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://10.87.196.180:8080/api/"

    fun create(context: Context): Retrofit {
        val userPreferences = UserPreferences(context)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client =      OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor {
                        runBlocking { userPreferences.getAccessToken() }
                    })
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}