package com.grig.recipesandroid.domain.model

import com.grig.recipesandroid.data.model.dto.CategoryValueDto

data class Recipe(
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val categoryValueIds: List<CategoryValue>,
//    val categoryValueIds: List<CategoryValueDto>,
//    val categories: List<Category>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<String>
//    val steps: MutableList<String>
)
