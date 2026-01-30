package com.grig.recipesandroid.data.api

//import com.grig.recipesandroid.data.model.dto.CategoryDto
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.data.model.request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.dto.UnitDto
import com.grig.recipesandroid.data.model.request.SearchByIngredientsRequest
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("/api/recipes")
    suspend fun getRecipes(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PagedRecipesResponse

    @GET("/api/recipes/my/recipes")
    suspend fun getMyRecipes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("direction") direction: String = "DESC"
    ) : PagedRecipesResponse

    @GET("/api/recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Long) : RecipeDto

    @POST("/api/recipes")
    suspend fun createRecipe(
        @Body request: RecipeCreateRequest
    ) : RecipeDto

    @PUT("/api/recipes/{id}")
    suspend fun updateRecipe(
        @Path("id") id: Long,
        @Body rerequest: RecipeUpdateRequest
    )

    @DELETE("/api/recipes/{id}")
    suspend fun deleteRecipe(
        @Path("id") id: Long
    )

//    //  Поиск рецептов по ингредиентам
    @POST("/api/recipes/search/by-ingredients")
    suspend fun searchByIngredients(
        @Body request: SearchByIngredientsRequest
    ): List<RecipeDto>


//  ++++++++++++++++++++++++
//    Favorites recipes API

    @GET("/api/favorites")
    suspend fun getFavoritesAll(): List<RecipeDto>
//    suspend fun getFavoritesAll(): List<Favorite>

    @POST("/api/favorites/{recipeId}")
    suspend fun addFavorite(@Path("recipeId") recipeId: Long): Unit

    @DELETE("/api/favorites/{recipeId}")
    suspend fun removeFavorite(@Path("recipeId") recipeId: Long): Unit

//    ++++++++++++++++++++
//    Category

    @GET("/api/category-value")
    suspend fun getCategoryValues(): List<CategoryValueDto>
//    @GET("api/categories/all")
//    suspend fun getCategories(): List<CategoryDto>

    @GET("/api/category-value/{id}")
    suspend fun getCategoryValuesById(@Path("id") id: Long) : CategoryValueDto

    @GET("/api/category-type")
    suspend fun getCategoryTypes(): List<CategoryTypeDto>

    @GET("/api/category-type/{id}")
    suspend fun getCategoryTypeById(@Path("id") id: Long) : CategoryTypeDto

//    ++++++++++++++++
//    INGREDIENT

    @GET("/api/ingredients/all")
//    @GET("api/ingredients")
    suspend fun getIngredients(): List<IngredientDto>

    @GET("/api/ingredients/{ingredientId}")
    suspend fun getIngredientById(@Path("ingredientId") ingredientId: Long): IngredientDto

//    ++++++++++++++++
//    UNIT
    @GET("/api/units")
    suspend fun getUnits(): List<UnitDto>


}