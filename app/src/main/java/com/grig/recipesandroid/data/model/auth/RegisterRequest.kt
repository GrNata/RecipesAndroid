package com.grig.recipesandroid.data.model.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null
)
