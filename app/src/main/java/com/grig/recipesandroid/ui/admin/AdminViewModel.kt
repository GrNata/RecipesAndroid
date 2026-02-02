package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import com.grig.recipesandroid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _usersAll = MutableStateFlow<List<UserRequest>>(emptyList())
    val usersAll: StateFlow<List<UserRequest>> = _usersAll

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _usersAll.value = authRepository.getAllUsers()
                Log.d("ADMIN", "AdminViewModel: usersAll: ${_usersAll.value}")

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

//    fun updateRole(userId: Long, newRole: UpdateUserRoleRequest) {
    fun updateRole(userId: Long, newRole: UpdateUserRoleResponse) {
        Log.d("ADMIN", "AdminViewModel: newRole: ${newRole}")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                authRepository.updateRoleUser(userId, newRole)
                // Обновляем список пользователей
                loadUsers()
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun updateBlockedUser(userId: Long, blocked: BlockUserRequest) {
        _loading.value = true
        _error.value = null
        try {
            authRepository.updateBlockedUser(userId, blocked)
            loadUsers()
        } catch (e: Exception) {
            _error.value = e.message
            Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
        } finally {
            _loading.value = false
        }
    }
}