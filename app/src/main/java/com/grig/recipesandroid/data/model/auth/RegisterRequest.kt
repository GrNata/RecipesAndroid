package com.grig.recipesandroid.data.model.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null,
    val userInfo: UserInfoResponse = UserInfoResponse(email, setOf("USER"))
)

data class RegisterUserRequest(
    val username: String,
    val email: String,
    val password: String? = null
)
