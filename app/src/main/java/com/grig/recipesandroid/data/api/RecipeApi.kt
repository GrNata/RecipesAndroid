package com.grig.recipesandroid.data.api

import com.grig.recipesandroid.data.model.dto_request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeDto
import com.grig.recipesandroid.data.model.dto_request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("api/recipes")
    suspend fun getRecipes(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PagedRecipesResponse

    @GET("api/recipes/my/recipes")
    suspend fun getMyRecipes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("direction") direction: String = "DESC"
    ) : PagedRecipesResponse

    @GET("api/recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Long) : RecipeDto

    @POST("api/recipes")
    suspend fun createRecipe(
        @Body request: RecipeCreateRequest
    )

    @PUT("api/recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") id: Long,
        @Body rerequest: RecipeUpdateRequest
    )

    @DELETE("api/recipes/{id}")
    suspend fun deleteRecipe(
        @Path("id") id: Long
    )


//  ++++++++++++++++++++++++
//    Favorites recipes API

    @GET("api/favorites")
    suspend fun getFavoritesAll(): List<RecipeDto>
//    suspend fun getFavoritesAll(): List<Favorite>

    @POST("api/favorites/{recipeId}")
    suspend fun addFavorite(@Path("recipeId") recipeId: Long): Unit

    @DELETE("api/favorites/{recipeId}")
    suspend fun removeFavorite(@Path("recipeId") recipeId: Long): Unit


}