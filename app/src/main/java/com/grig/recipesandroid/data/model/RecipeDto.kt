package com.grig.recipesandroid.data.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("categories") val categories: List<CategoryDto>?,
    @SerializedName("ingredients") val ingredients: List<IngredientWithAmountDto>?,
    @SerializedName("steps") val steps: List<String>?
)
