package com.example.mynotesapplication.backend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("email") val email: String, // Matches "email" in JSON
    @SerialName("password") val password: String
)
