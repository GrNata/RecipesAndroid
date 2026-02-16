package com.grig.recipesandroid.data.model.request

import com.grig.recipesandroid.data.model.dto.RecipeStatus

data class RecipeUpdateRequest(
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int?,
    val categoryIds: List<Long>,
    val ingredients: List<IngredientRequest>,
    val steps: List<String>
)

data class RecipeUpdateStatusRequest(
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int?,
    val status: RecipeStatus,
    val categoryIds: List<Long>,
    val ingredients: List<IngredientRequest>,
    val steps: List<String>
)

data class RecipeStatusrequest (
    val status: RecipeStatus
)

