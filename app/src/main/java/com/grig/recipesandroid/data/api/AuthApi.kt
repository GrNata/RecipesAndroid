package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.AuthResponse
import com.grig.recipesandroid.data.model.LoginRequest
import com.grig.recipesandroid.data.model.RegisterRequest
import com.grig.recipesandroid.data.model.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest) : AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest) : TokenResponse

    @POST("api/auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: String) : TokenResponse

    @POST("api/auth/logout")
    suspend fun logout(@Body refreshToken: String)
}