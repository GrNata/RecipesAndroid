package com.grig.recipesandroid.data.repository

import android.util.Log
import androidx.compose.ui.semantics.Role
import com.grig.recipesandroid.data.api.AuthApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.model.auth.AuthTokens
import com.grig.recipesandroid.data.model.auth.AuthTokensWithRole
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.LoginRequest
import com.grig.recipesandroid.data.model.auth.RefreshTokenRequest
import com.grig.recipesandroid.data.model.auth.RegisterRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val api: AuthApi,
    private val tokenRepository: TokenRepository
) {

//    suspend fun register(request: RegisterRequest) : AuthTokens {
    suspend fun register(request: RegisterRequest) : AuthTokensWithRole {
        val response = api.register(request)
        tokenRepository.saveTokens(response.accessToken, response.refreshToken, response.userInfo)
        return AuthTokensWithRole(response.accessToken, response.refreshToken, response.userInfo.roles)
//        return AuthTokens(response.accessToken, response.refreshToken)
    }

//    suspend fun login(request: LoginRequest) : AuthTokens {
    suspend fun login(request: LoginRequest) : AuthTokensWithRole {
        val response = api.login(request)
        tokenRepository.saveTokens(
            response.accessToken,
            response.refreshToken,
            response.userInfo
        )
        return AuthTokensWithRole(response.accessToken, response.refreshToken, response.userInfo.roles)
//        return AuthTokens(response.accessToken, response.refreshToken)
    }

//    suspend fun refreshToken(): String {
    suspend fun refreshToken(): AuthTokensWithRole {
        val refresh = tokenRepository.refreshToken.first() ?: throw Exception("No refresh token")
        val response = api.refreshToken(RefreshTokenRequest(refresh))
//        val response = api.refreshToken(refresh)
        tokenRepository.saveTokens(response.accessToken, response.refreshToken, response.userInfo)
        return AuthTokensWithRole(response.accessToken, response.refreshToken, response.userInfo.roles)
//        return response.accessToken
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

}