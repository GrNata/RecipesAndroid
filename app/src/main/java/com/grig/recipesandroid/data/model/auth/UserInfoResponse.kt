package com.grig.recipesandroid.data.model.auth

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("email") val email: String,
    @SerializedName("roles") val roles: Set<String>
)
