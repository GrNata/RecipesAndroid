package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Warning


//   разделяем UI и state - RecipeListScreen
//	RecipeListContent — чистый UI, ничего не знает про ViewModel или PagingSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListContent(
    recipes: LazyPagingItems<Recipe>,
    query: String,
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

//            -------- ???????
            //        Error State для refresh (анимированный)
            val isError = recipes.loadState.refresh is LoadState.Error
            if (isError) {
//            if (recipes.loadState.refresh is LoadState.Error) {
                item {
//                    val isError = recipes.loadState.refresh is LoadState.Error

                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFAC3B61),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ошибка загрузки данных",
                                color = Color(0xFFAC3B61)
                            )

                        }
                    }
                }
            }

//            Empty Search State с анимацией
            item {
                val  isEmptySearch =
                            recipes.itemCount == 0 &&
                            recipes.loadState.refresh is LoadState.NotLoading &&
                            query.isNotBlank()
                val isEmptyAll =
                        recipes.itemCount == 0 &&
                        recipes.loadState.refresh is LoadState.NotLoading &&
                        query.isBlank()

                if (isEmptySearch || isEmptyAll) {
                    AnimatedVisibility(
                        visible = isEmptySearch,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        EmptyState(
                            icon = if (isEmptySearch) Icons.Outlined.Search else Icons.Default.Search,
                            title = if (isEmptySearch) "Ничего не найдено \uD83D\uDD0D" else "Список пуст",
                            subtitle = if (isEmptySearch) "Попробуй изменить запрос" else "Добавьте рецепты, чтобы начать"
                        )
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
        }       // LazyColumn
    }
}

//Сделать Empty state:
//	•	с иконкой
//	•	с анимацией появления
//	•	НЕ ломая Paging
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFAC3B61),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF123C69)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}