package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("api/recipes")
    suspend fun getRecipes(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PagedRecipesResponse

//    @GET("api/recipes")
//    suspend fun getRecipes(): PagedRecipesResponse

    @GET("api/recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Long) : RecipeDto

}