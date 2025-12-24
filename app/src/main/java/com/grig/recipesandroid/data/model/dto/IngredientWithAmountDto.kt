package com.grig.recipesandroid.data.model.dto

data class IngredientWithAmountDto(
//    val id: Long?,
    val id: Long,
    val name: String,
    val amount: String?,
    val unit: UnitDto
)
