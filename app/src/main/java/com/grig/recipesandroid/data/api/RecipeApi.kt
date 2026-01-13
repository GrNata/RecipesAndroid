package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.dto.Favorite
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

//    Favorites recipes API

    @GET("api/favorites")
    suspend fun getFavoritesAll(): List<RecipeDto>
//    suspend fun getFavoritesAll(): List<Favorite>

    @POST("api/favorites/{recipeId}")
    suspend fun addFavorite(@Path("recipeId") recipeId: Long): Unit

    @DELETE("api/favorites/{recipeId}")
    suspend fun removeFavorite(@Path("recipeId") recipeId: Long): Unit
}