package com.grig.recipesandroid.data.model.request

data class RecipeUpdateRequest(
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int?,
    val categoryIds: List<Long>,
    val ingredients: List<IngredientRequest>,
    val steps: List<String>
)