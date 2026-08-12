package com.example.mynotesapplication.backend.network

import kotlinx.serialization.Serializable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// The data you send to the server (your refresh token).
// Field name matches the backend's AuthController.RefreshRequest("refreshToken") exactly,
// since the Retrofit client here uses the kotlinx.serialization converter (not Gson).
@Serializable
data class RefreshRequest(
    val refreshToken: String
)

// The data the server sends back (new tokens), matching AuthService.TokenPair's field names.
@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

interface AuthApiService {
    @POST("/auth/refresh")
    fun refreshToken(
        @Body request: RefreshRequest
    ): Call<TokenResponse>
    // ^ IMPORTANT: Must return Call<...>, not Response<...> or suspend
}