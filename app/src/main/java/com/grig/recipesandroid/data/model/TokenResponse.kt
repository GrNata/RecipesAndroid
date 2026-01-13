package com.grig.recipesandroid.data.model

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)
