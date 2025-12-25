package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.grig.recipesandroid.domain.model.Recipe
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


//   разделяем UI и state - RecipeListScreen
//	RecipeListContent — чистый UI, ничего не знает про ViewModel или PagingSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListContent(
    recipes: LazyPagingItems<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
//    логика отображения индикатора сверху
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading
//    Paging повторно подгружает данные
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { recipes.refresh() }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(count = recipes.itemCount) { index ->
                val recipe = recipes[index]
                recipe?.let {
                    RecipeItem(recipe = it) {
                        onRecipeClick(it)
                    }
                }
            }

            when (recipes.loadState.append) {
                is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                is LoadState.Error -> {
                    item { Text("Ошибка загрузки") }
                }
                else -> Unit
            }
        }
    }


}