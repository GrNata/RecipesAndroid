package com.grig.recipesandroid.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.flow.StateFlow
import java.lang.Exception

class RecipePagingSource(
    private val api: RecipeApi,
    private val query: String
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        return try {
            val page = params.key ?: 0
//            Log.d("PAGING", "Loading page=$page size=${params.loadSize}")
            val response = api.getRecipes(page = page, size = params.loadSize)

//            Фильтруем по query прямо здесь
            val recipes = response.content
                .map { it.toDomain() }
                .filter { it.name.contains(query, ignoreCase = true) }
            LoadResult.Page(
                data = recipes,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}