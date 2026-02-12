package com.grig.recipesandroid.data.model.dto

import com.google.gson.annotations.SerializedName
import com.grig.recipesandroid.domain.model.RecipeAuthor

data class RecipeDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("createdAt")val createdAt: String,  //  уже отформатированная дата
    @SerializedName("publishedAt")val publishedAt: String?,   // Дата публикации (null, если не опубликован)
    @SerializedName("status")val status: RecipeStatus,  //  Статус (DRAFT, PENDING, APPROVED, REJECTED)
    @SerializedName("author")val author: RecipeAuthor,
    @SerializedName("baseServings") val baseServings: Int?,
    @SerializedName("categoryValues") val categories: Map<String, CategoryValueDto>?,
    @SerializedName("ingredients") val ingredients: List<IngredientWithAmountDto>?,
    @SerializedName("steps") val steps: List<String>?,

    @SerializedName("totalCalories") val totalCalories: Int?
)

