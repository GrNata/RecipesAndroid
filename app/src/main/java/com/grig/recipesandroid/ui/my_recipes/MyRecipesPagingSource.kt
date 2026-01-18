package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.Flow

class MyRecipesPagingSource(
    private val repository: RecipeRepository,
//    private val accessTokenFlow: StateFlow<String?>   // <- токен
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val page = params.key ?: 0
        return try {
//            val token = accessTokenFlow.value ?: return LoadResult.Error(Exception("No access token"))
//            val token = accessTokenFlow.value
//            if (token.isNullOrBlank()) {
//                return LoadResult.Error(Exception("No access token"))
//            }

            val response = repository.getMyRecipes(
                page = page,
                size = params.loadSize
//                ,
//                token = token
            )


            Log.d("MY Recipes MyRecipesPagingSource", "response size: ${response.size}")

            val recipes = response.content.map { it.toDomain() }    // конвертируем DTO в Recipe
            LoadResult.Page(
                data = recipes,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("MY Recipes MyRecipesPagingSource", "response - error: $e}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? =
        state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
}