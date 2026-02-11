package com.grig.recipesandroid.ui.admin.auditLogs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto
import com.grig.recipesandroid.data.model.auth.AuditLogFilteruiState
import com.grig.recipesandroid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class AdminAuditViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {



    //    +++++++++++++++++
    //    АУДИТ-ЛОГ

    private val _logs = MutableStateFlow<List<AdminAuditLogDto>>(emptyList())
    val logs: StateFlow<List<AdminAuditLogDto>> = _logs

    private val _filter = MutableStateFlow(AuditLogFilteruiState())
    val filter = _filter.asStateFlow()

    private val _selectedActionType = MutableStateFlow<String?>(null)
    var selectedActionType = _selectedActionType

    private val _selectedEntityType = MutableStateFlow<String?>(null)
    var selectedEntityType = _selectedEntityType

    private val _selectedFrom = MutableStateFlow<String?>("")
    var selectedFrom = _selectedFrom

    private val _selectedTo = MutableStateFlow<String?>("")
    var selectedTo = _selectedTo


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

    fun setActionType(valeu: String?) {
        _filter.update { it.copy(actionType = valeu) }
    }

    fun setEntityType(valeu: String?) {
        _filter.update { it.copy(entityType = valeu) }
    }

    fun setFrom(date: String?) {
        _filter.update { it.copy(from = date) }
    }

    fun setTo(date: String?) {
        _filter.update { it.copy(to = date) }
    }

    fun applyFilter() {
        Log.d("FILTER AUDIT", "AdminAuditViewModel: _filter = ${_filter.value}")

        viewModelScope.launch {
            try {
                _logs.value = authRepository.filtredAuditLogs(
                    actionType =  _filter.value.actionType,
                    entityType = _filter.value.entityType,
                    from = _filter.value.from,
                    to = _filter.value.to
                )
                _selectedActionType.value = _filter.value.actionType
                _selectedEntityType.value = _filter.value.entityType
                _selectedFrom.value = _filter.value.from
                _selectedTo.value = _filter.value.to

            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

}