package com.example.mynotesapplication.backend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @SerialName("id") val id: Long? = null,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("color") val color: Long,
)
