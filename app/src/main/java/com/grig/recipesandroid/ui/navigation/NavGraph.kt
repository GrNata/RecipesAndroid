package com.grig.recipesandroid.ui.navigation

import android.app.Application
import android.content.Context
import android.util.Log
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

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.FavoritesDataStore
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.auth.LoginScreen
import com.grig.recipesandroid.ui.auth.RegisterScreen
import com.grig.recipesandroid.ui.my_recipes.MyRecipesScreen
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModelFactory
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModul
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModel
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModelFactory
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModelFactory


@Composable
fun AppNavGraph(
    navController: NavHostController,
    api: RecipeApi,
    authViewModel: AuthViewModel,
    recipeRepository: RecipeRepository,
    tokenRepository: TokenRepository,
    applicationContext: Context,
    recipeViewModel: RecipesViewModel
    ) {

            NavHost(
                navController = navController,
                startDestination = "recipe_list"
//            startDestination = startDestination
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
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                ) {
                    val isAuth by authViewModel.isAuthenticated.collectAsState()
                    Log.d("LOGIN Log", "NavGraph (recipe_list) isAuthenticated = $isAuth")

                    RecipeListScreen(
//                        viewModel = viewModel<RecipesViewModel>(), // здесь создаём ViewModel
                        viewModel = viewModel(factory = RecipesViewModelFactory(
                            repository = recipeRepository,
                            favoritesRepository = FavoritesRepository(
                                api = api,
                                tokenRepository = tokenRepository,
                                local = FavoritesDataStore(applicationContext as Application)
                            ),
                            userIdFlow = authViewModel.userId
                        )),
                        navController = navController,
                        onRecipeClick = { recipeId ->
                            navController.navigate("recipe_detail/${recipeId}")
                        },
                        authViewModel = authViewModel
                    )
                }
                composable(
                    route = "recipe_detail/{recipeId}",
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.LongType }),
//            Animated NavHost
                    enterTransition = {
                        fadeOut(animationSpec = tween(1500))
                        slideInHorizontally(
//                    initialOffsetX = { it },
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

                    //  ОБЩИЙ RecipesViewModel (тот же, что в списке)
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("recipe_list")
                    }

                    val recipeViewModel: RecipesViewModel = viewModel(parentEntry)

                    //  Detail ViewModel (отдельный)
                    val detailViewModel: RecipeDetailViewModel = viewModel(
//                factory = RecipeDetailViewModelFactory(recipeRepository, recipeId)
                        factory = RecipeDetailViewModelFactory(api = api, recipeId = recipeId)
                    )

                    RecipeDetailScreen(
                        recipeId = recipeId,
                        recipeViewModel = recipeViewModel,
                        viewModelDetailRecipe = detailViewModel,
                        authViewModel = authViewModel,
                        navController = navController,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("login") {
                    val isAuth by authViewModel.isAuthenticated.collectAsState()
                    LoginScreen(
                        navController = navController
                    )
//                    LoginScreen(
//                        onLoginSuccess = { navController.popBackStack() }
//                    )
                }

                composable("register") {
                    RegisterScreen(onRegisterSuccess = { navController.navigate("recipe_list") })
                }

                composable("my_recipes") {
                    Log.d("NAV MyRecipe", "Entered MyRecipesScreen composable")
                    val myRecipesViewModel: MyRecipesViewModul = viewModel(
                        factory = MyRecipesViewModelFactory(recipeRepository)
                    )
                    MyRecipesScreen(
                        myViewModul = myRecipesViewModel,
                        recipeViewModel = recipeViewModel,
                        navController = navController,
                        authViewModel = authViewModel
                    )
                }

            }       //  NavHost
}