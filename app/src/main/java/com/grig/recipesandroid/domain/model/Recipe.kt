package com.grig.recipesandroid.domain.model

import com.grig.recipesandroid.data.model.dto.RecipeStatus

data class Recipe(
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val createdAt: String,  //  уже отформатированная дата

    val publishedAt: String?,   // Дата публикации (null, если не опубликован)
    val status: RecipeStatus,  //  Статус (DRAFT, PENDING, APPROVED, REJECTED)


    val author: RecipeAuthor,
    val baseServings: Int?,
    val categories: List<CategoryValue>,
    val ingredients: List<RecipeIngredient>,
    val steps: List<String>,

    val totalCalories: Int?
)

data class RecipeAuthor(
    val id: Long,
    val username: String
)