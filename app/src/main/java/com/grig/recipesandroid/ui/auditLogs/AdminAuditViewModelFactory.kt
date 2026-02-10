package com.grig.recipesandroid.ui.auditLogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.ui.admin.AdminViewModel

class AdminAuditViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminAuditViewModel::class.java)) {
            return AdminAuditViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}