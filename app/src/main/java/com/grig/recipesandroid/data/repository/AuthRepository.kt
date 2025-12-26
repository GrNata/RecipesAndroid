package com.grig.recipesandroid.data.repository

import com.grig.recipesandroid.data.api.AuthApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.model.AuthTokens
import com.grig.recipesandroid.data.model.LoginRequest
import com.grig.recipesandroid.data.model.RegisterRequest
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val api: AuthApi,
    private val tokenRepository: TokenRepository
) {

    suspend fun register(request: RegisterRequest) : AuthTokens {
        val response = api.register(request)
        tokenRepository.saveTokens(response.accessToken, response.refreshToken)
        return AuthTokens(response.accessToken, response.refreshToken)
    }

    suspend fun login(request: LoginRequest) : AuthTokens {
        val response = api.login(request)
        tokenRepository.saveTokens(response.accessToken, response.refreshToken)
        return AuthTokens(response.accessToken, response.refreshToken)
    }

    suspend fun refreshToken(): String {
        val refresh = tokenRepository.refreshToken.first() ?: throw Exception("No refresh token")
        val response = api.refreshToken(refresh)
        tokenRepository.saveTokens(response.accessToken, response.refreshToken)
        return response.accessToken
    }

    suspend fun logout() {
        val refresh = tokenRepository.refreshToken.first() ?: return
        api.logout(refresh)
        tokenRepository.clearTokens()
    }
}