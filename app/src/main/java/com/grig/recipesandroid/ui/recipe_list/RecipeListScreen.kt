package com.grig.recipesandroid.ui.recipe_list


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.auth.LoginScreen

//   разделяем UI и state - RecipeListContent
// RecipeListScreen получает ViewModel и передаёт данные в RecipeListContent.
@Composable
fun RecipeListScreen(
    viewModel: RecipesViewModel,
    navController: NavController,
    onRecipeClick: (Long) -> Unit,
    authViewModel: AuthViewModel
) {
    // 🔹 флаг для отображения LoginScreen
    var showLoginScreen by remember { mutableStateOf(false) }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState(initial = null)

    Log.d("СЕРДЦЕ - TOKEN RecipeListScreen", "accessToken = ${accessToken?.take(10)}")

    val favoritesSet by viewModel.favorites.collectAsState()

    val recipes = viewModel.recipesPagingFlow.collectAsLazyPagingItems()
    val query by viewModel.query.collectAsState()

//    фильтрация избранных
    var showOnlyFavorites by remember { mutableStateOf(false) }

//    LaunchedEffect(accessToken) {
//        if (!accessToken.isNullOrBlank()) {
//            viewModel.loadFavorites()
//        } else {
//            viewModel.clearFavorites()
//        }
//    }
    val userId by authViewModel.userId.collectAsState()

////     синхронизация избранного local + remote
//    LaunchedEffect(isAuthenticated) {
//        if (isAuthenticated) {
////            viewModel.syncFavoritesIfLoggerdIn(true)
//            viewModel.syncFavoritesIfLoggedIn(userId)
//        }
//    }

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
            Log.d("СЕРДЦЕ RecipeListScreen", "RecipeListScreen isAuthenticated = $isAuthenticated")
            Log.d("CICLE RecipeListScreen", "RecipeListScreen isAuthenticated = $isAuthenticated")
            AppTopBar(
                title = "Рецепты",
                isAuthenticated = isAuthenticated,
                showMyRecipes = false,
//                onLoginClick = { showLoginScreen = true },
                onLoginClick = { navController.navigate("login") },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onMyRecipesClick = {
                    Log.d("NAV MyRecipe", "Navigating to MyRecipesScreen")
                    navController.navigate("my_recipes") {
                        launchSingleTop = true
                    }
                },
//                authViewModel = authViewModel
            )
        }
    ) { paddingValues ->

        // 🔹 показываем LoginScreen если нужно
//        if (showLoginScreen) {
//            LoginScreen(
////                viewModel = authViewModel,
//                authViewModel = authViewModel,
//                navController
////                onLoginSuccess = {
////                    // закрываем экран логина
////                    showLoginScreen = false
////                }
//            )
//        } else {

            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //        поиск / фильтрация
                    OutlinedTextField(
                        value = query,
                        onValueChange = { newText ->
                            viewModel.setQuery(newText)
                        },
                        modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
                            .weight(1f),
                        placeholder = {
                            Text("Поиск рецептов…")
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    //      кнопка фильтрации избранного
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = { showOnlyFavorites = !showOnlyFavorites },
                        label = { Text("Избранное") }
                    )

                    //            Log.d("СЕРДЦЕ - 4", "FavoriteSet = $favoritesSet")
                }

                RecipeListContent(
                    viewModel = viewModel,
                    recipes = recipes,
                    query = query,
                    favorites = favoritesSet,       //  StateFlow из RecipesViewModel
                    onFavoriteClick = { recipeId -> viewModel.toggleFavorite(recipeId) },
                    showOnlyFavorites = showOnlyFavorites,
                    onToggleFavoritesFilter = {
                        showOnlyFavorites = !showOnlyFavorites
                    },
                    onRecipeClick = { id ->
                        Log.e("ИЩУ:", "RecipeListScreen: id = ${id} navigate to recipe_detail/$id")
                        navController.navigate("recipe_detail/$id")
                    }
                )

            }
//        }           // else
    }

}






