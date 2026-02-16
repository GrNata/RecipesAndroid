package com.grig.recipesandroid.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.request.SearchByIngredientsRequest
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import com.grig.recipesandroid.data.paging.RecipePagingSource
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RecipeRepository(
    private val api: RecipeApi
) {

//    fun getRecipesPaper(query: String = "") : Pager<Int, Recipe> {
//    fun getRecipesPaper(query: String = "") : Flow<PagingData<RecipeDto>> {
    fun getRecipesPaper(query: String = "") : Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { RecipePagingSource(api, query) }
        ).flow
//        return Pager(
//            config = PagingConfig(pageSize = 10),
//            pagingSourceFactory = { RecipePagingSource(api, query) }
//        )
    }

    suspend fun getMyRecipes(
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "id",
        direction: String = "DESC"
//        ,
//        token: String
    ) : PagedRecipesResponse {
//        val token = tokenRepository.accessToken.first() ?: throw Exception("No access token"
        return api.getMyRecipes(page, size, sortBy, direction)
//        return api.getMyRecipes(page, size)
    }

    suspend fun getRecipesById(id: Long) : Recipe {
        val dto = api.getRecipeById(id)
        return dto.toDomain()
    }

    suspend fun createRecipe(request: RecipeCreateRequest): RecipeDto {
//        api.createRecipe(request)
        return api.createRecipe(request)
    }


    suspend fun updateRecipe(
        recipeId: Long,
        request: RecipeUpdateRequest
    ) {
        api.updateRecipe(recipeId, request)
    }

    suspend fun deleteRecipe(recipeId: Long): Response<Unit> = api.deleteRecipe(recipeId)


//    Поиск рецептов по ингредиентам
    suspend fun searchRecipesByIngredients(request: SearchByIngredientsRequest) : List<RecipeDto> {
        val response = api.searchByIngredients(request)
    return response
}

//    +++++++++++++++++
//    MODERATOR
//    Отправить на модерацию
    suspend fun sendToModeration(id: Long): RecipeDto =
        api.sendModeration(id)

//    MODERATOR - получить список рецептов на проверку
    suspend fun getPendingRecipes(page: Int, size: Int) : PagedRecipesResponse {
    Log.d("MODERATOR", "RecipeRepository: page: ${page}, size: $size")
        val response = api.getPendingRecipes(page, size)
        Log.d("MODERATOR", "RecipeRepository: response: ${response}")
        return response
    }

//    MODERATOR - обобрить
    suspend fun approveRecipe(id: Long): RecipeDto =
        api.approveRecipe(id)

//    MODERATOR - отклонить
    suspend fun rejectRecipe(id: Long): RecipeDto =
        api.rejectRecipe(id)

//    +++++++++++++++++

}