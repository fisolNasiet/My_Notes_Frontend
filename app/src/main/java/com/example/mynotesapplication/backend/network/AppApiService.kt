package com.example.mynotesapplication.backend.network

import com.example.mynotesapplication.backend.model.Note
import com.example.mynotesapplication.backend.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppApiService {

    @GET("/notes")
    suspend fun getNotes(): Response<List<Note>>

    @POST("/notes")
    suspend fun createNote(@Body note: Note): Response<Note>

    @DELETE("/notes/{id}")
    suspend fun deleteNote(@Path("id") id: Long): Response<Void>

    @POST("/auth/register")
    suspend fun createUser(@Body user: User): Response<Void>


    @POST("/auth/login")
    suspend fun loginUser(@Body user: User): Response<TokenResponse>

    @DELETE("/auth/me")
    suspend fun deleteAccount(): Response<Void>
}