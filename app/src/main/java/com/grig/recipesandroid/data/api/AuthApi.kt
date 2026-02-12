package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto
import com.grig.recipesandroid.data.model.auth.AdminStatisticsDto
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.LoginRequest
import com.grig.recipesandroid.data.model.auth.RefreshTokenRequest
import com.grig.recipesandroid.data.model.auth.RegisterResponse
import com.grig.recipesandroid.data.model.auth.RegisterUserRequest
import com.grig.recipesandroid.data.model.auth.TokenResponse
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthApi {

    @POST("api/auth/register")
//    suspend fun register(@Body request: RegisterRequest) : AuthResponseWithRole
//    suspend fun register(@Body request: RegisterUserRequest) = AuthResponseWithRole
    suspend fun register(@Body request: RegisterUserRequest) : RegisterResponse
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

    @GET("/api/admin/users/by_email/{email}")
    suspend fun getUserByEmail(@Path("email") email : String?) : UserRequest?

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

    @GET("/api/admin/users/role")
    suspend fun getUsersWithRole(@Query("role") role: String) : List<UserRequest>

    @GET("/api/admin/users/blocked")
    suspend fun getUsersWithBlocked(@Query("blocked") blocked: Boolean) : List<UserRequest>

    @GET("/api/admin/users/filter")
    suspend fun getUsersFiltred(
        @Query("role") role: String?,
        @Query("blocked") blocked: Boolean?,
        @Query("email") email: String?,
        @Query("lastLoginFrom") lastLoginFrom: String?,
        @Query("lastLoginTo") lastLoginTo: String?
    ): List<UserRequest>
//    suspend fun getUsersFiltred(@Query("role") role: String?, @Query("blocked") blocked: Boolean?): List<UserRequest>

//    Аудит - логи
    @GET("/api/admin/audit")
    suspend fun getAllAuditLogs(): List<AdminAuditLogDto>

    @GET("/api/admin/audit/filter")
    suspend fun filteredAuditLogs(
        @Query("actionType") actiontype: String?,
        @Query("entityType") entityType: String?,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): List<AdminAuditLogDto>

    //    Статистика
    @GET("/api/admin/statistics")
    suspend fun AdminSatistics(): AdminStatisticsDto

}