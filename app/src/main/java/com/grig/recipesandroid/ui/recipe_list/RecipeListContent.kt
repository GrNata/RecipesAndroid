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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
            // 1️⃣ Список рецептов
            items(count = recipes.itemCount) { index ->
                val recipe = recipes[index]
                recipe?.let {
                    RecipeItem(recipe = it) {
                        onRecipeClick(it.id)
                    }
                }
            }
            // Loader снизу (append) - при подгрузке следующей страницы
            when (recipes.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        )
                    }
                }
                is LoadState.Error -> {
                    item {
                        Text(
                            text = "Ошибка загрузки",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                            )
                    }
                }
                else -> Unit
            }
//        }
        // Empty State: список пуст
        if (recipes.itemCount == 0 &&
            recipes.loadState.refresh is LoadState.NotLoading
            ) {
            item {
                Text(
                    text = "Ничего не найдено",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }

//        Error State для refresh
        if (recipes.loadState.refresh is LoadState.Error) {
//            Box(modifier = Modifier.fillMaxSize()) {
//            }
            item {
                Text(
                    text = "Ошибка загрузки данных",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center,
                    color = Color(0xFFAC3B61)
                )
            }
        }
        }
    }


}