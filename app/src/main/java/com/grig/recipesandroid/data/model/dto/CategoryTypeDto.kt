package com.grig.recipesandroid.data.model.dto

import com.google.gson.annotations.SerializedName

data class CategoryTypeDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nameType") val nameType: String
)
