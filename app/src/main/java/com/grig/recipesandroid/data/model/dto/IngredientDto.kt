package com.grig.recipesandroid.data.model.dto

import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
