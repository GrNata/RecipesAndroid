package com.grig.recipesandroid.data.model.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
data class AuthResponseWithRole(
    val accessToken: String,
    val refreshToken: String,
    val userInfo: UserInfoResponse
)
