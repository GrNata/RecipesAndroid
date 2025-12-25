package com.grig.recipesandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailScreen
import com.grig.recipesandroid.ui.recipe_list.RecipeListScreen
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "recipe_list"
    ) {
        composable("recipe_list") {
            RecipeListScreen(
                viewModel = viewModel<RecipesViewModel>(), // здесь создаём ViewModel
                navController = navController,
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/${recipeId}")
                }
            )
        }
        composable(
            route = "recipe_detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.LongType} )
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L
            RecipeDetailScreen(
                recipeId = recipeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}