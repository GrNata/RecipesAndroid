package com.grig.recipesandroid.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.model.auth.AuthTokens
import com.grig.recipesandroid.data.model.auth.LoginRequest
import com.grig.recipesandroid.data.model.auth.RegisterRequest
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.utils.JwtUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AuthViewModel(
//class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    val isAuthenticated: StateFlow<Boolean> = tokenRepository.refreshToken
        .map { !it.isNullOrBlank() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val accessToken = tokenRepository.accessToken

//    // email текущего пользователя или null если не залогинен
    val userId: StateFlow<String?> = accessToken
        .map { tokens ->
        // Распарси токен и достань email/userId, или null
        tokens?.let { JwtUtils.getEmailFromToken(it) }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)


    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tokens = MutableStateFlow<AuthTokens?>(null)
    val tokens: StateFlow<AuthTokens?> = _tokens


    // ✅ Новый флаг
    private val _authStateRestored = MutableStateFlow(false)
    val authStateRestored: StateFlow<Boolean> = _authStateRestored

//    для -  «возврата на экран с которого повторное логирование»
    private val _pandingRoute = MutableStateFlow<String?>(null)
    val pendingRoute: StateFlow<String?> = _pandingRoute


    init {
        restoreSession()
        Log.d("CICLE AuthViewModel", "AuthViewModel - init")
    }

    fun login(email: String, password: String) {
        Log.d("CICLE AuthViewModel", "AuthViewModel - login")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val request = LoginRequest(email, password)
//                authRepository.login(request)
                val result = authRepository.login(request)
                _tokens.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(email: String, password: String, name: String? = null) {
        Log.d("CICLE AuthViewModel", "AuthViewModel - register")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val request = RegisterRequest(email, password, name)
                authRepository.register(request)
//                val result = authRepository.register(request)
//                _tokens.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        Log.d("CICLE AuthViewModel", "AuthViewModel - logout")
        viewModelScope.launch {
            tokenRepository.clearTokens()

            // ВАЖНО: сбрасываем стейты
            _tokens.value = null
            _loading.value = false
            _error.value = null
//            authRepository.logout()
//            _tokens.value = null

        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val refreshToken = tokenRepository.refreshToken.first()

            // нет refreshToken → пользователь НЕ залогинен
            if (refreshToken.isNullOrBlank()) {
                return@launch
            }

            try {
                // тихо обновляем accessToken
                authRepository.refreshToken()
                // accessToken сохранён внутри TokenRepository
            } catch (e: java.lang.Exception) {
                //  refreshToken протух → вычищаем сессию
                tokenRepository.clearTokens()
            }
            // ✅ после попытки восстановления ставим флаг в true
            _authStateRestored.value = true
        }
    }


    //    для -  «возврата на экран с которого повторное логирование»
    fun requireLogin(route: String) {
        _pandingRoute.value = route
    }

    fun consumePendingRoute() : String? {
        val route = _pandingRoute.value
        _pandingRoute.value = null
        return route
    }

//    private fun restoreSession() {
//        viewModelScope.launch {
//            val access = tokenRepository.accessToken.firstOrNull()
//            val refresh = tokenRepository.refreshToken.firstOrNull()
//
//            if (!access.isNullOrBlank()) {
//                _isAuthenticated.value = true
//                _accessToken.value = access
//            } else {
//                _isAuthenticated.value = false
//                _accessToken.value = null
//            }
//
//            if (!refresh.isNullOrBlank()) {
//                try {
//                    val newAccess = authRepository.refreshToken()
//                    _accessToken.value = newAccess
//                    _isAuthenticated.value = true
//                } catch (e: Exception) {
//                    tokenRepository.clearTokens()
//                    _isAuthenticated.value = false
//                }
//            }
//
//            // ✅ После восстановления состояния ставим флаг в true
//            _authStateRestored.value = true
//        }
//    }
}

//Этот ViewModel умеет:
//	•	Логин / Регистрация
//	•	Хранит токены (_tokens)
//	•	Управляет состоянием загрузки и ошибок