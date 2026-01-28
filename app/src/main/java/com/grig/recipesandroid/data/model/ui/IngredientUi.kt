package com.grig.recipesandroid.data.model.ui

data class IngredientUi(
    val id: Long,
    val name: String,
    val nameEng: String?,
    val amount: Double?,
    val unit: String?,
    val energyKcal100g: Int?
)
