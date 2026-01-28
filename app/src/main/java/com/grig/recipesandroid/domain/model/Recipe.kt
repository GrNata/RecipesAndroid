package com.grig.recipesandroid.domain.model

data class Recipe(
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int?,
    val categories: List<CategoryValue>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<String>,

    val totalCalories: Int?
)
