package com.grig.recipesandroid.domain.model

data class CategoryValue(
    val id: Long,
    val categoryTypeId: Long,
    val categoryTypeName: String,
    val categoryValue: String
)
