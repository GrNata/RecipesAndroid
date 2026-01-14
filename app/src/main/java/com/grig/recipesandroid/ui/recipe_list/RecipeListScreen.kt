package com.grig.recipesandroid.ui.recipe_list


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.grig.recipesandroid.ui.auth.AuthViewModel

//   разделяем UI и state - RecipeListContent
// RecipeListScreen получает ViewModel и передаёт данные в RecipeListContent.
@Composable
fun RecipeListScreen(
    viewModel: RecipesViewModel,
    navController: NavController,
    onRecipeClick: (Long) -> Unit,
    authViewModel: AuthViewModel
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState(initial = null)
    Log.d("СЕРДЦЕ - TOKEN RecipeListScreen", "accessToken = ${accessToken?.take(10)}")

    val favoritesSet by viewModel.favorites.collectAsState()

    val recipes = viewModel.recipesPagingFlow.collectAsLazyPagingItems()
    val query by viewModel.query.collectAsState()

    LaunchedEffect(accessToken) {
        if (!accessToken.isNullOrBlank()) {
            viewModel.loadFavorites()
        } else {
            viewModel.clearFavorites()
        }
    }

//  В Scaffold передаём snackbarHost = { SnackbarHost(hostState = snackbarHostState) }.
//	•	LaunchedEffect(message) слушает messageFlow из ViewModel и показывает Snackbar.
//	•	Кнопка Retry на Snackbar вызывает viewModel.retryRecipes()
    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.messageFlow.collectAsState("")

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            // Показываем Snackbar
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retryRecipes()        //      вызываем retry при клике на Retry
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            RecipeListTopBar(
                title = "Рецепты",
                isAuthenticated = isAuthenticated,
                onLoginClick = { navController.navigate("login") },
                onLogoutClick = { authViewModel.logout() }
            )
        }
    ) {
        paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
//        поиск / фильтрация
            OutlinedTextField(
                value = query,
                onValueChange = { newText ->
                    viewModel.setQuery(newText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = {
                    Text("Поиск рецептов…")
                },
                singleLine = true
            )

//            Log.d("СЕРДЦЕ - 4", "FavoriteSet = $favoritesSet")

            RecipeListContent(
                viewModel = viewModel,
                recipes = recipes,
                query = query,
                favorites = favoritesSet,       //  StateFlow из RecipesViewModel
                onFavoriteClick = { recipeId -> viewModel.toggleFavorite(recipeId) },
                onRecipeClick = { id ->
                    Log.e("ИЩУ:", "RecipeListScreen: id = ${id} navigate to recipe_detail/$id")
                    navController.navigate("recipe_detail/$id")
                }
            )
        }
    }

}






