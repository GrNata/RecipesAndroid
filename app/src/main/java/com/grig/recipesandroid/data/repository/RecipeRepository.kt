package com.grig.recipesandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.model.dto_request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import com.grig.recipesandroid.data.paging.RecipePagingSource
import com.grig.recipesandroid.domain.model.Recipe

class RecipeRepository(
    private val api: RecipeApi
) {

    fun getRecipesPaper(query: String = "") : Pager<Int, Recipe> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { RecipePagingSource(api, query) }
        )
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

    suspend fun createRecipe(request: RecipeCreateRequest) {
        api.createRecipe(request)
    }

    suspend fun updateRecipe(
        recipeId: Long,
        request: RecipeUpdateRequest
    ) {
        api.updateRecipe(recipeId, request)
    }

    suspend fun deleteRecipe(recipeId: Long) {
        api.deleteRecipe(recipeId)
    }


    // Временный метод для отладки
    suspend fun getRecipesRaw(): String {
        // Создаем простой OkHttp запрос
        val request = okhttp3.Request.Builder()
            .url("http://10.0.2.2:9090/api/recipes")
            .build()
        val client = okhttp3.OkHttpClient()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    fun getRecipesRawBlocking(): String {
        val client = okhttp3.OkHttpClient()
        val request = okhttp3.Request.Builder()
            .url("http://10.0.2.2:9090/api/recipes")
            .build()
        val response = client.newCall(request).execute() // блокирующий вызов
        return response.body?.string() ?: ""
    }

}