package com.grig.recipesandroid.ui.adminStatistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.auth.AdminStatisticsDto
import com.grig.recipesandroid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminStatsViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _stats = MutableStateFlow<AdminStatisticsDto?>(null)
    val stats: StateFlow<AdminStatisticsDto?> = _stats

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error = _error

    fun loadStatistics() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _stats.value = authRepository.getAdminStatistics()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}