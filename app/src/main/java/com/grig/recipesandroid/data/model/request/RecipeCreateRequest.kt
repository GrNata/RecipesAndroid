package com.grig.recipesandroid.data.model.request

data class RecipeCreateRequest(
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int?,
    val categoryValueIds: List<Long>,
    val ingredients: List<IngredientRequest>,
    val steps: List<String>
)