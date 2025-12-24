package com.grig.recipesandroid.data.repository

import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.model.RecipeDto
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse

class RecipeRepository(
    private val api: RecipeApi
) {

    suspend fun getRecipes() : List<RecipeDto> {
        return api.getRecipes().content
    }
//    suspend fun getRecipes() : PagedRecipesResponse= api.getRecipes()


    suspend fun getRecipesById(id: Long) : RecipeDto = api.getRecipeById(id)

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