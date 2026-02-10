package com.grig.recipesandroid.ui.admin.auditLogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto
import com.grig.recipesandroid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminAuditViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {



    //    +++++++++++++++++
    //    АУДИТ-ЛОГ

    private val _logs = MutableStateFlow<List<AdminAuditLogDto>>(emptyList())
    val logs: StateFlow<List<AdminAuditLogDto>> = _logs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var _error = MutableStateFlow<String?>(null)
    var error: StateFlow<String?> = _error

    fun loadLogs() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _logs.value = authRepository.getAllAuditLogs()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

}