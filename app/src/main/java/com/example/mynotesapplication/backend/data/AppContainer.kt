package com.example.mynotesapplication.backend.data

import android.content.Context
import com.example.mynotesapplication.backend.network.AppApiService
import com.example.mynotesapplication.backend.network.AuthApiService
import com.example.mynotesapplication.backend.security.AuthInterceptor
import com.example.mynotesapplication.backend.security.TokenAuthenticator
import com.example.mynotesapplication.backend.security.TokenManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val appRepository: AppRepository
    val tokenManager: TokenManager
}

// 1. Pass Context here so we can create the TokenManager
class DefaultAppContainer(context: Context) : AppContainer {


    //private val BASE_URL = "http://54.167.32.13"
    private val BASE_URL = "http://54.167.32.13"

    // 2. Initialize TokenManager
    override val tokenManager = TokenManager(context)

    // 3. Create the Auth API (Used specifically inside the Authenticator)
    // Note: I switched this to use Kotlin Serialization to match your main Retrofit
    private val authRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val authApiService = authRetrofit.create(AuthApiService::class.java)

    // 4. Create the OkHttp Client (The Security Layer)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenManager))
        .authenticator(TokenAuthenticator(tokenManager, authApiService))
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    // 5. Create Main Retrofit
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .client(okHttpClient) // <--- CRITICAL FIX: You must attach the client here!
        .build()

    private val retrofitService: AppApiService by lazy {
        retrofit.create(AppApiService::class.java)
    }

    override val appRepository: AppRepository by lazy {
        NetworkRepository(retrofitService)
    }
}