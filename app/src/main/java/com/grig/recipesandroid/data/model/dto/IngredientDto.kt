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

data class IngredientRequest(
    val name: String,
    val nameEng: String?,
    val energyKcal100g: Int? = 0
)

data class IngredientUpdate(
    val id: Long?,
    var name: String,
    var nameEng: String?,
    var energyKcal100g: Int? = 0
)

data class IngredientAddEdit(
    val id: Long?,
    var name: String,
    var nameEng: String?,
    var energyKcal100g: Int? = 0
)
