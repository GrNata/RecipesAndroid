package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    recipeViewModel: RecipesViewModel,
    viewModelDetailRecipe: RecipeDetailViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    onBack : () -> Unit
) {

    // Создаём scaffoldState для SnackBar
    val snackbarHostState = remember { SnackbarHostState() }

    val isAuthenticated by authViewModel
        .isAuthenticated
        .collectAsState()

//    val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L
//    Log.d("DETAIL_SCREEN", "recipeId=$recipeId")

    val recipe by viewModelDetailRecipe.recipe.collectAsState()
    val loading by viewModelDetailRecipe.loading.collectAsState()
    val error by viewModelDetailRecipe.error.collectAsState()

//    // Загружаем рецепт при первом отображении
//    LaunchedEffect(recipeId) {
////        viewModel.loadRecipe(recipeId)
//        viewModel.loadRecipe()
//    }

    // Передаём в Scaffold
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        RecipeDetailContent(
            recipe = recipe,
            loading = loading,
            error = error,
            isAuthenticated = isAuthenticated,
            onBack = onBack,
            navController = navController,
            authViewModel = authViewModel,
            recipeViewModel = recipeViewModel,
            recipeId = recipeId,
            snackbarHostState = snackbarHostState
        )
    }



}