package com.grig.recipesandroid.ui.navigation

//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.compose.rememberNavController

import androidx.navigation.navArgument
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
//import androidx.navigation.compose.animation.AnimatedNavHost

@Composable
fun AppNavGraph(navController: NavHostController) {

//    AnimatedNavHost(
    NavHost(
        navController = navController,
        startDestination = "recipe_list"
    ) {
        composable(
            route = "recipe_list",
            exitTransition = {
                fadeOut(animationSpec = tween(500))
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(500))
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing)
                )
            }
//            Animated NavHost
//            enterTransition = { fadeIn() },
//            exitTransition = { fadeOut() }
            ) {
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
            arguments = listOf(
                navArgument("recipeId") { type = NavType.LongType} ),
//            Animated NavHost
            enterTransition = {
                fadeOut(animationSpec = tween(1500))
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
//                slideInVertically { it } + fadeIn()
            },
            popExitTransition = {
                fadeIn(animationSpec = tween(1500))
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }
//            exitTransition = {
//                slideOutVertically { it } + fadeOut()
//            }
        ) { backStackEntry ->

            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L

            RecipeDetailScreen(
                recipeId = recipeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}