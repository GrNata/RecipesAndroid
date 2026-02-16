package com.grig.recipesandroid.data.repository

import android.util.Log
import com.grig.recipesandroid.data.api.AuthApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto
import com.grig.recipesandroid.data.model.auth.AdminStatisticsDto
import com.grig.recipesandroid.data.model.auth.AuthTokensWithRole
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.LoginRequest
import com.grig.recipesandroid.data.model.auth.RefreshTokenRequest
import com.grig.recipesandroid.data.model.auth.RegisterResponse
import com.grig.recipesandroid.data.model.auth.RegisterUserRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import kotlinx.coroutines.flow.first
import retrofit2.Response

class AuthRepository(
    private val api: AuthApi,
    private val tokenRepository: TokenRepository
) {
    suspend fun register(request: RegisterUserRequest) : RegisterResponse = api.register(request)

    suspend fun login(request: LoginRequest) : AuthTokensWithRole {
        val response = api.login(request)
        tokenRepository.saveTokens(
            response.accessToken,
            response.refreshToken,
            response.userInfo
        )
        return AuthTokensWithRole(response.accessToken, response.refreshToken, response.userInfo.roles)
    }

//    suspend fun refreshToken(): String {
    suspend fun refreshToken(): AuthTokensWithRole {
        val refresh = tokenRepository.refreshToken.first() ?: throw Exception("No refresh token")
        val response = api.refreshToken(RefreshTokenRequest(refresh))
        tokenRepository.saveTokens(response.accessToken, response.refreshToken, response.userInfo)
        return AuthTokensWithRole(response.accessToken, response.refreshToken, response.userInfo.roles)
    }

    suspend fun logout() {
        val refresh = tokenRepository.refreshToken.first() ?: return
        api.logout(refresh)
        tokenRepository.clearTokens()
    }

    //    +++++++++++++++
//      ADMIN

    suspend fun getAllUsers(): List<UserRequest> {
        val response = api.getUsers()
        Log.d("ADMIN", "AuthRepository: users: ${response}")
        return response
    }

//    suspend fun updateRoleUser(id: Long, role: UpdateUserRoleRequest) =
    suspend fun updateRoleUser(id: Long, role: UpdateUserRoleResponse) =
        api.updateRoleUser(id, role)

    suspend fun updateBlockedUser(id: Long, blocked: BlockUserRequest) =
        api.updateBlockedUser(id, blocked)

    suspend fun getUsersWithRole(role: String): List<UserRequest> {
        return api.getUsersWithRole(role)
    }

    suspend fun getUsersWithBlocked(blocked: Boolean) : List<UserRequest> = api.getUsersWithBlocked(blocked)

    suspend fun getUsersFiltred(
        role: String?,
        blocked: Boolean?,
        email: String?,
        lastLoginFrom: String?,
        lastLoginTo: String?
    ): List<UserRequest> =
        api.getUsersFiltred(role, blocked, email, lastLoginFrom, lastLoginTo)
//    suspend fun getUsersFiltred(role: String?, blocked: Boolean?): List<UserRequest> = api.getUsersFiltred(role, blocked)

    suspend fun getUserByEmail(email: String?) : UserRequest? =
        api.getUserByEmail(email)

    suspend fun deleteUser(id: Long): Response<Unit> = api.deleteUser(id)

//    АУДИТ - ЛОГИ
    suspend fun getAllAuditLogs() : List<AdminAuditLogDto> =
        api.getAllAuditLogs()

    suspend fun filtredAuditLogs(
        actionType: String?,
        entityType: String?,
        from: String?,
        to: String?
        ) : List<AdminAuditLogDto> =
        api.filteredAuditLogs(
            actiontype = actionType,
            entityType = entityType,
            from = from,
            to = to
        )

//    Статистика
    suspend fun getAdminStatistics() : AdminStatisticsDto =
        api.AdminSatistics()


}