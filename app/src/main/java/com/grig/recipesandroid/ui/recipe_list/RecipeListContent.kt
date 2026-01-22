package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.grig.recipesandroid.ui.utilRecipe.CategoryTypeDropDown
import com.grig.recipesandroid.ui.utilRecipe.ShimmerRecipeItem


//   разделяем UI и state - RecipeListScreen
//	RecipeListContent — чистый UI, ничего не знает про ViewModel или PagingSource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RecipeListContent(
    viewModel: RecipesViewModel,
    recipes: LazyPagingItems<Recipe>,
    query: String,
    favorites: Set<Long>,
    onFavoriteClick: (Long) -> Unit,
    showOnlyFavorites: Boolean,
    onToggleFavoritesFilter: () -> Unit,
    onRecipeClick: (Long) -> Unit

) {
    //    логика отображения индикатора сверху
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading
//    Paging повторно подгружает данные
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val lastToggleRecipeId by viewModel.lastToggleRecipeId.collectAsState()

    // создаём SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedCategoryTypeId by remember { mutableStateOf(1L) }
    val categoryTypesAll = viewModel.categoryTypesAll

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

//LazyColumn / SwipeRefresh тут
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { recipes.refresh() }
        ) {

            val filteredRecipes =
                if (showOnlyFavorites) {
                recipes.itemSnapshotList.items.filter { favorites.contains(it.id) }
                } else {
                    recipes.itemSnapshotList.items
                }

            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                // Dropdown для выбора группировки
                CategoryTypeDropDown(
                    categoryTypes = categoryTypesAll,
                    selectedId = selectedCategoryTypeId,
                    onSelected = { selectedCategoryTypeId = it }
                )

                //            Группируем рецепты по первой категории (можно доработать для нескольких)
                val grouped = filteredRecipes
                    .flatMap { recipe ->
                        Log.d(
                            "CATEGORY-ch", "RecipeListContent: category:" +
                                    " ${recipe.categories}"
                        )
                        recipe.categories
                            .filter { it.categoryTypeId == selectedCategoryTypeId }
//                                .filter { it.categoryTypeId == 1L }
                            .map { it.categoryValue to recipe }       // создаём пары category -> recipe
                    }
                    .groupBy(
                        keySelector = { it.first },
                        valueTransform = { it.second }
                    )
//            }

                // Основной список
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF7EDE9))
                ) {

                    Log.d(
                        "CATEGORY-ch",
                        "RecipeListContent: start recipes: ${recipes.itemSnapshotList.items}"
                    )
                    Log.d(
                        "CATEGORY-ch",
                        "RecipeListContent: start filteredRecipes: ${filteredRecipes}"
                    )
                    Log.d(
                        "CATEGORY-ch", "RecipeListContent: start filteredRecipes:" +
                                " ${
                                    filteredRecipes.forEach {
                                        it.categories.forEach { it.categoryValue }
                                    }
                                }"
                    )


                    if (recipes.loadState.refresh is LoadState.Loading) {
                        items(5) { ShimmerRecipeItem() }
                    } else {

//                    // Dropdown для выбора группировки
//                    CategoryTypeDropDown(
//                        categoryTypes = categoryTypesAll,
//                        selectedId = selectedCategoryTypeId,
//                        onSelected = { selectedCategoryTypeId = it }
//                    )

////            Группируем рецепты по первой категории (можно доработать для нескольких)
//                    val grouped = filteredRecipes
//                        .flatMap { recipe ->
//                            Log.d(
//                                "CATEGORY-ch", "RecipeListContent: category:" +
//                                        " ${recipe.categories}"
//                            )
//                            recipe.categories
//                                .filter { it.categoryTypeId == selectedCategoryTypeId }
////                                .filter { it.categoryTypeId == 1L }
//                                .map { it.categoryValue to recipe }       // создаём пары category -> recipe
//                        }
//                        .groupBy(
//                            keySelector = { it.first },
//                            valueTransform = { it.second }
//                        )

                        grouped.forEach { (category, recipesInCategory) ->

// Sticky Header для категории - объединение рецептов по категориям
                            stickyHeader {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFEFEFEF))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = category,
//                                    text = category.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF123C69)
                                    )
                                }
                            }
//                Рецепты в категории - стандартные карточки рецептов
                            items(recipesInCategory) { recipe ->
                                val fav = favorites

//                            // Локальная реактивная переменная isFavorite для этого конкретного RecipeItem
//                            val isFavotite by remember {
//                                derivedStateOf { favorites.contains(recipe.id) }
//                            }

//                            Log.d("RecipeItem", "ListRecipe recipe: ${recipe.ingredients.size}")
//                            Log.d("RecipeItem", "ListRecipe recipe: ${recipe.ingredients.forEach {
//                                (it.unit?.label) ?: ""
//                            }}")

                                RecipeItem(
                                    viewModel = viewModel,
                                    recipe = recipe,
                                    query = query,
                                    isFavorite = favorites.contains(recipe.id),
                                    isOwner = false,
//                                isFavorite = isFavotite,
                                    onFavoriteClick = { viewModel.toggleFavorite(recipe.id) },
                                    onClick = { onRecipeClick(recipe.id) }
                                )

                                Log.d(
                                    "СЕРДЦЕ - RecipeListContent",
                                    "Favorite size = ${favorites.size}"
                                )
//                            for (l in favorites) {
//                                Log.d("СЕРДЦЕ - 2", "Favorite-ID = $l, recipeId = ${recipe.id}")
//                            }
                            }
                        }

//            -------- ???????
                        //        Error State для refresh (анимированный)
                        val isError = recipes.loadState.refresh is LoadState.Error
                        if (isError) {
                            item {
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
                            val isEmptySearch =
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

                    }       //   else
                }       // LazyColumn
            }   //      Column
        }
    }

//      Использовать SnackbarHost в Scaffold или простой Toast при клике на избранное.
    LaunchedEffect(favorites) {
        if (lastToggleRecipeId != null) {
            val message = if (favorites.contains(lastToggleRecipeId))
                                    "Рецепт добавлен в избранное"
                                  else "Рецепт удален из избранного"
            snackbarHostState.showSnackbar(message)
        }
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