package com.example.mynotesapplication.backend.network

import com.example.mynotesapplication.backend.model.Note
import com.example.mynotesapplication.backend.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppApiService {

    @GET("/notes")
    suspend fun getNotes(): Response<List<Note>>

    @POST("/notes")
    suspend fun createNote(@Body note: Note): Response<Note>

    @POST("/auth/register")
    suspend fun createUser(@Body user: User): Response<Void>


    @POST("/auth/login")
    suspend fun loginUser(@Body user: User): Response<TokenResponse>
}