package com.grig.recipesandroid.data.model.dto_request

data class RecipeCreateRequest(
    val name: String,
    val description: String?,
    val image: String?,
    val categoryIds: List<Long>,
    val ingredients: List<IngredientRequest>,
    val steps: List<String>
)
