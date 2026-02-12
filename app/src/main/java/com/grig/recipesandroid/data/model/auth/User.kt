package com.grig.recipesandroid.data.model.auth

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class User(
    val id: Long,
    val email: String,
    val name: String? = null
)

data class UserRequest(
    @SerializedName("id") val id: Long?,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("registrationDate") val registrationDate: String,
    @SerializedName("lastLoginAt") val lastLoginAt: String,
    @SerializedName("roles") val roles: List<String>,
    @SerializedName("blocked") val blocked: Boolean
)

data class UpdateUserRoleRequest(
    @SerializedName("name") val roles: List<String>  //  ADMIN, USER
)

data class UpdateUserRoleResponse(
    val roles: Set<String>
)

data class BlockUserRequest(
    val blocked: Boolean
)

data class RoleUserRequest(
    val role: String
)


