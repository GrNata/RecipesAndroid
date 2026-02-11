package com.grig.recipesandroid.data.model.auth

data class AdminAuditLogDto(
    val id: Long,
    val adminEmail: String,
    val actionType: String,
    val entityType: String,
    val entityId: Long?,
    val description: String,
    val createdAt: String
)

data class AuditLogFilteruiState(
    val actionType: String? = null,
    val entityType: String? = null,
    val from: String? = null,   //  dd-MM-yyyy
    val to: String? = null   //  dd-MM-yyyy
)
