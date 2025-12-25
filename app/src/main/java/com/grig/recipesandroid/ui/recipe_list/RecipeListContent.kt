package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.grig.recipesandroid.domain.model.Recipe
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.graphics.Color

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


//   разделяем UI и state - RecipeListScreen
//	RecipeListContent — чистый UI, ничего не знает про ViewModel или PagingSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListContent(
    recipes: LazyPagingItems<Recipe>,
    onRecipeClick: (Long) -> Unit

) {
//    логика отображения индикатора сверху
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading
//    Paging повторно подгружает данные
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { recipes.refresh() }
    ) {
        // Основной список
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7EDE9))
            ) {

            items(count = recipes.itemCount) { index ->
                val recipe = recipes[index]
                recipe?.let {
                    RecipeItem(recipe = it) {
                        onRecipeClick(it.id)
                    }
                }
            }
            // Loader снизу (append)
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
        // Empty State: список пуст
        if (recipes.itemCount == 0 && recipes.loadState.refresh !is LoadState.Loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Список рецептов пуст",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFAC3B61)
                )
            }
        }

//        Error State для refresh
        if (recipes.loadState.refresh is LoadState.Error) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Ошибка загрузки данных",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFFAC3B61)
                )
            }
        }
    }


}