package com.grig.recipesandroid.data.model

data class IngredientWithCaloriesAndAmount(
    val id: Long,
    val name: String,
    val energyKcal100g: Int?,
    val amount: Double?,
    val unitCode: String?
)
