package com.grig.recipesandroid.data.api

import com.google.gson.GsonBuilder
import com.grig.recipesandroid.data.model.auth.AuthResponse
import com.grig.recipesandroid.data.model.auth.AuthResponseWithRole
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.LoginRequest
import com.grig.recipesandroid.data.model.auth.RefreshTokenRequest
import com.grig.recipesandroid.data.model.auth.RegisterRequest
import com.grig.recipesandroid.data.model.auth.TokenResponse
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import com.grig.recipesandroid.utils.LocalDateTimeAdapter
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDateTime


interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest) : AuthResponseWithRole
//    suspend fun register(@Body request: RegisterRequest) : AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest) : TokenResponse

    @POST("api/auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest) : TokenResponse
//    suspend fun refreshToken(@Body refreshToken: String) : TokenResponse

    @POST("api/auth/logout")
    suspend fun logout(@Body refreshToken: String)


//    +++++++++++++++
//      ADMIN

    @GET("/api/admin/users")
    suspend fun getUsers(): List<UserRequest>

    @PUT("/api/admin/users/{id}/roles")
    suspend fun updateRoleUser(
        @Path("id") id: Long,
        @Body request: UpdateUserRoleResponse
//        @Body request: UpdateUserRoleRequest
        )

    @PUT("/api/admin/users/{id}/block")
    suspend fun updateBlockedUser(
        @Path("id") id: Long,
        @Body request: BlockUserRequest
    )


}