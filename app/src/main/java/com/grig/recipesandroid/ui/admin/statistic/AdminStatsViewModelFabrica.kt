package com.grig.recipesandroid.ui.admin.statistic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.AuthRepository

class AdminStatsViewModelFabrica(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminStatsViewModel::class.java)) {
            return AdminStatsViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}