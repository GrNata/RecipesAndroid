package com.grig.recipesandroid.data.model.auth

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userInfo") val userInfo: UserInfoResponse
)
//data class TokenResponse(
//    val accessToken: String,
//    val refreshToken: String
//)

data class RefreshTokenRequest(
    val refreshToken: String
)
