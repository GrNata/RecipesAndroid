package com.grig.recipesandroid.data.repository

import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.model.dto.IngredientDto

class IngredientRepository(private val api: RecipeApi) {

    suspend fun getAllIngredients(): List<IngredientDto>  = api.getIngredients()
//    {
//        val ingredientsDto = api.getIngredients()
//        return ingredientsDto.map { dto ->
//            Ingredient(
//                id = dto.id ?: 0L,
//                name = dto.name
//            )
//        }
//    }

//    suspend fun getAllIngredients(): List<IngredientDto> {
//        return api
//    }

    suspend fun getIngredientById(id: Long): IngredientDto {
        return api.getIngredientById(id)
    }
}