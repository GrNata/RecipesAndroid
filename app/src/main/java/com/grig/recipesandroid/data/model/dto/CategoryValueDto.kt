package com.grig.recipesandroid.data.model.dto

data class CategoryValueDto(
    val id: Long,
    val typeId: Long,
    val typeName: String,
    val categoryValue: String
)

data class CategoryValueCreate(
    val typeId: Long,
    val typeName: String,
    val categoryValue: String
)

data class CategoryValueUpdate(
    val id: Long,
    val typeId: Long,
    val typeName: String,
    val categoryValue: String
)

