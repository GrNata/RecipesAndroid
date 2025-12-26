package com.grig.recipesandroid.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grig.recipesandroid.data.model.AuthTokens
import com.grig.recipesandroid.data.model.LoginRequest
import com.grig.recipesandroid.data.model.RegisterRequest
import com.grig.recipesandroid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tokens = MutableStateFlow<AuthTokens?>(null)
    val tokens: StateFlow<AuthTokens?> = _tokens

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val request = LoginRequest(email, password)
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
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val request = RegisterRequest(email, password, name)
                val result = authRepository.register(request)
                _tokens.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _tokens.value = null
        }
    }
}

//Этот ViewModel умеет:
//	•	Логин / Регистрация
//	•	Хранит токены (_tokens)
//	•	Управляет состоянием загрузки и ошибок