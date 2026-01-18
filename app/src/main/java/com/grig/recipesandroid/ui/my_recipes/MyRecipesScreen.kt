package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipeItem
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyRecipesScreen(
    myViewModul: MyRecipesViewModel,
    recipeViewModel: RecipesViewModel,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    Log.d("VM_CHECK", "recipeViewModel: $recipeViewModel")  // ← проверьте, что не null


//    val myRecipes by myViewModul.myRecipes.collectAsState(initial = emptyList())
    val myRecipes = myViewModul.myRecipesPagingFlow.collectAsLazyPagingItems()
    val favorites by recipeViewModel.favorites.collectAsState()


//    Для проверки
    LaunchedEffect(myRecipes.loadState) {
        Log.d(
            "MY_RECIPES_LOAD_STATE",
            """
        refresh = ${myRecipes.loadState.refresh}
        append  = ${myRecipes.loadState.append}
        prepend = ${myRecipes.loadState.prepend}
        """
                .trimIndent()
        )
    }

    // Optional: фильтр по избранным
    var showOnlyFavorites by remember { mutableStateOf(false) }

//    val filteredRecipes =
//        if (showOnlyFavorites) {
//            myRecipes.filter { favorites.contains(it.id) }
//        } else {
//            myRecipes
//        }
    val filteredRecipes = myRecipes.itemSnapshotList.items ?: emptyList()
//    val filteredRecipes = myRecipes.itemSnapshotList.items.filterNotNull().let { list ->
//        if (showOnlyFavorites) list.filter { favorites.contains(it.id) }
//        else list
//    }

    Log.d("MY Recipes SIZE", "filteredRecipes size: ${filteredRecipes.size}")
    Log.d("MY Recipes SIZE", "myRecipes size: ${myRecipes.itemSnapshotList.size}")

//    группировка по категориям
    val grouped = filteredRecipes
        .flatMap { recipe ->
            recipe.categories.map { category -> category to recipe }
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

//    val loading by myViewModul.loading.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Мои рецепты",
                isAuthenticated = true,
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                onShareClick = {},
                authViewModel = authViewModel
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            if (myRecipes.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    grouped.forEach { (category, recipesInCategory) ->
                        stickyHeader {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFEFEFEF))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF123C69)
                                )
                            }
                        }
//                            }

                        items(recipesInCategory) { recipe ->
                            RecipeItem(
                                viewModel = recipeViewModel,
                                recipe = recipe,
                                query = "",
                                isFavorite = favorites.contains(recipe.id),         //  по желанию
                                onFavoriteClick = { recipeViewModel.toggleFavorite(recipe.id) },
                                onClick = { navController.navigate("recipe_detail/${recipe.id}") }
//                                    onEditClick = { /* открыть экран редактирования */  },
//                                    onDeleteClick = {  /* вызвать репозиторий delete */ }
                            )
                        }
                    }
                }           // if
            }
        }
    }
}
