package com.example.mynotesapplication.backend.security

import com.example.mynotesapplication.backend.network.AuthApiService
import com.example.mynotesapplication.backend.network.RefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenManager: TokenManager,
    // Inject a separate Retrofit instance for auth calls to avoid circular dependencies
    private val authApi: AuthApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Auth endpoints are public; a 401 here means bad credentials/an invalid refresh
        // token, not an expired access token, so refreshing and retrying would never help
        // and would otherwise loop until OkHttp's follow-up limit throws.
        if (response.request.url.encodedPath.startsWith("/auth/")) return null

        // Give up after a couple of attempts instead of retrying forever if the server
        // keeps rejecting the refreshed token.
        if (responseCount(response) >= 2) return null

        // 1. Get the current refresh token
        val refreshToken = tokenManager.getRefreshToken() ?: return null // Trigger logout if no refresh token

        // 2. Synchronously call the API to refresh the token
        // Note: authenticate() runs on a background thread, so blocking calls are allowed.
        val refreshResponse = authApi.refreshToken(RefreshRequest(refreshToken)).execute()

        return if (refreshResponse.isSuccessful) {
            val newTokens = refreshResponse.body()

            // 3. Save the new tokens
            newTokens?.let {
                tokenManager.saveTokens(it.accessToken, it.refreshToken)

                // 4. Retry the original request with the NEW access token
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }
        } else {
            // Refresh failed (Refresh token expired or revoked)
            tokenManager.clearTokens()
            // TODO: Trigger a global event to navigate user to Login Screen
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }
}