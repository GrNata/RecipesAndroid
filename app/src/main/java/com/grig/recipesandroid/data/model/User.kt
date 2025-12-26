package com.grig.recipesandroid.data.model

data class User(
    val id: Long,
    val email: String,
    val name: String? = null
)
