package com.grig.recipesandroid.domain.model

//  Связь рецепт ↔ ингредиент

data class RecipeIngredient(
    val ingredient: Ingredient,
    val amount: String?,
    val unit: String?
)
