package com.grig.recipesandroid.domain.model

data class Recipe(
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val categories: List<Category>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<String>
)
