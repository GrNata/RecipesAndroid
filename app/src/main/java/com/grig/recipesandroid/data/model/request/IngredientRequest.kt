package com.grig.recipesandroid.data.model.request

data class IngredientRequest(
    val ingredientId: Long,
    val amount: String?,
    val unitId: Long?
//    val unit: String?
)