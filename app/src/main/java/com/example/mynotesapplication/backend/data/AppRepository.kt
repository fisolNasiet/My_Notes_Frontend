package com.example.mynotesapplication.backend.data

import com.example.mynotesapplication.backend.model.Note
import com.example.mynotesapplication.backend.model.User
import com.example.mynotesapplication.backend.network.AppApiService
import com.example.mynotesapplication.backend.network.TokenResponse
import retrofit2.Response

interface AppRepository {
    suspend fun login(email: String, password: String): Response<TokenResponse>
    suspend fun
            register(email: String, password: String): Response<Void>
    suspend fun getNotes(): Response<List<Note>>
    suspend fun createNote(note: Note): Response<Note>
    suspend fun deleteNote(id: Long): Response<Void>
    suspend fun deleteAccount(): Response<Void>
}

class NetworkRepository(
    private val noteApiService: AppApiService
) : AppRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): Response<TokenResponse> = noteApiService.loginUser(User(email, password))

    override suspend fun register(
        email: String,
        password: String,
    ): Response<Void> = noteApiService.createUser(User(email, password))

    override suspend fun getNotes(): Response<List<Note>> = noteApiService.getNotes()

    override suspend fun createNote(note: Note): Response<Note> = noteApiService.createNote(note)

    override suspend fun deleteNote(id: Long): Response<Void> = noteApiService.deleteNote(id)

    override suspend fun deleteAccount(): Response<Void> = noteApiService.deleteAccount()
}