package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {

    @GET("api/recipes")
//    suspend fun getRecipes(): List<RecipeDto>
    suspend fun getRecipes(): PagedRecipesResponse

    @GET("api/recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Long) : RecipeDto

}