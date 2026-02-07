package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    recipeViewModel: RecipesViewModel,
    viewModelDetailRecipe: RecipeDetailViewModel,
//    recipeDetailViewModel: RecipeDetailViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    onBack : () -> Unit
) {

    // Создаём scaffoldState для SnackBar
    val snackbarHostState = remember { SnackbarHostState() }

    val isAuthenticated by authViewModel
        .isAuthenticated
        .collectAsState()
//    recipeDetailViewModel.loadRecipe()

    val recipe by viewModelDetailRecipe.recipe.collectAsState()
    val loading by viewModelDetailRecipe.loading.collectAsState()
    val error by viewModelDetailRecipe.error.collectAsState()

    // Если authState еще не восстановлен, показываем загрузку
    if (!isAuthenticated) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    // Передаём в Scaffold
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->


        Log.d("22-ИЩУ:", "RecipeDetailScreen: before RecipeDetailContent recipeId=${recipeId}")
        Log.d("22-ИЩУ:", "RecipeDetailScreen: before RecipeDetailContent recipe: ${recipe}")

        RecipeDetailContent(
            recipe = recipe,
            loading = loading,
            error = error,
            isAuthenticated = isAuthenticated,
            onBack = onBack,
            navController = navController,
            authViewModel = authViewModel,
            recipeViewModel = recipeViewModel,
            recipeDetailViewModel = viewModelDetailRecipe,
            recipeId = recipeId,
            snackbarHostState = snackbarHostState
        )
    }

}