package com.grig.recipesandroid.data.model

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null
)
