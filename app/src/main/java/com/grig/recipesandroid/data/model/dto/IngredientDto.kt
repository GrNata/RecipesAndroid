package com.grig.recipesandroid.data.model.dto

import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("nameEnglish") val nameEng: String?,
//    @SerializedName("name_eng") val nameEng: String?,
    @SerializedName("energyKcal100g") val energyKcal100g: Int?
//    @SerializedName("energy_kcal_100g") val energyKcal100g: Int?

)
