package com.grig.recipesandroid.ui.navigation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.ui.admin.AddEditCategoryScreen
import com.grig.recipesandroid.ui.admin.AddEditCategoryTypeScreen
import com.grig.recipesandroid.ui.admin.AddEditIngredientScreen
import com.grig.recipesandroid.ui.admin.AdminScreen
import com.grig.recipesandroid.ui.admin.AdminViewModel
import com.grig.recipesandroid.ui.admin.CategoryAdminScreen
import com.grig.recipesandroid.ui.admin.IngredientAdminScreen
import com.grig.recipesandroid.ui.admin.NewUpdateCategoryTypeScreen
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.auth.LoginScreen
import com.grig.recipesandroid.ui.auth.RegisterScreen
import com.grig.recipesandroid.ui.my_recipes.AddEditRecipeViewModel
import com.grig.recipesandroid.ui.my_recipes.AddEditRecipeScreen
import com.grig.recipesandroid.ui.my_recipes.ImageScreen
import com.grig.recipesandroid.ui.my_recipes.IngredientsScreen
import com.grig.recipesandroid.ui.my_recipes.MyRecipesScreen
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModel
import com.grig.recipesandroid.ui.my_recipes.SelectCategoriesScreen
import com.grig.recipesandroid.ui.my_recipes.StepScreen
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModel
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModelFactory
import com.grig.recipesandroid.ui.search_by_ingredients.IngredientsForSearchScreen
import com.grig.recipesandroid.ui.search_by_ingredients.SearchByIngredientsViewModel
import com.grig.recipesandroid.ui.search_by_ingredients.SearchResultScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    api: RecipeApi,
    authViewModel: AuthViewModel,
    recipeRepository: RecipeRepository,
    categoryRepository: CategoryRepository,
    tokenRepository: TokenRepository,
    ingredientRepository: IngredientRepository,
    unitRepository: UnitRepository,
    applicationContext: Context,
    recipeViewModel: RecipesViewModel,
    addEditRecipeViewModel: AddEditRecipeViewModel,
    myRecipesViewModel: MyRecipesViewModel,
    searchByIngredientsViewModel: SearchByIngredientsViewModel,
    adminViewModel: AdminViewModel
//    recipeDetailViewModel: RecipeDetailViewModel
    ) {

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
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                ) {
                    val isAuth by authViewModel.isAuthenticated.collectAsState()
                    Log.d("СЕРДЦЕ NavGraph", "NavGraph (recipe_list) isAuthenticated = $isAuth")

                    RecipeListScreen(
                        viewModel = recipeViewModel,
                        navController = navController,
                        onRecipeClick = { recipeId ->
                            navController.navigate("recipe_detail/${recipeId}")
                            Log.d(
                                "3-ИЩУ:",
                                "NavGraph: recipe_list  навигация к детализации рецепта id = $recipeId"
                            )
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
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L

                    //  Detail ViewModel (отдельный)
                    val detailViewModel: RecipeDetailViewModel = viewModel(
                        factory = RecipeDetailViewModelFactory(api = api, recipeId = recipeId)
                    )

                    Log.d("1-ИЩУ:", "NavGraph (recipe_detail) navigate to RecipeDetailScreen, recipeId=$recipeId")
                    RecipeDetailScreen(
                        recipeId = recipeId,
                        recipeViewModel = recipeViewModel,
                        viewModelDetailRecipe = detailViewModel,
                        authViewModel = authViewModel,
//                        recipeDetailViewModel = recipeDetailViewModel,
                        navController = navController,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable("login") {
                    Log.e("CICLE NAV_TRACE", "NavGraph navigate to login ")
//                    val isAuth by authViewModel.isAuthenticated.collectAsState()
                    LoginScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )
//                    LoginScreen(
//                        onLoginSuccess = { navController.popBackStack() }
//                    )
                }

                composable("register") {
                    RegisterScreen(onRegisterSuccess = { navController.navigate("recipe_list") })
                }

//                if (isAuthenticated) {
                    composable("my_recipes") { backStackEntry ->
                        Log.d("NAV MyRecipe", "Entered MyRecipesScreen composable")

                        val parentEntry = remember(backStackEntry) {
                            navController.getBackStackEntry("recipe_list")
                        }

//                        val myRecipesViewModel: MyRecipesViewModel = viewModel(
//                            parentEntry,
//                            factory = MyRecipesViewModelFactory(recipeRepository, authViewModel)
//                        )

                        MyRecipesScreen(
                            myViewModul = myRecipesViewModel,
                            recipeViewModel = recipeViewModel,
                            navController = navController,
                            authViewModel = authViewModel,
                            addEditRecipeViewModel = addEditRecipeViewModel
                        )
                }

                composable("recipe_add") { backEntry ->
                    val parentEntry = remember(backEntry) {
                        navController.getBackStackEntry("my_recipes")
                    }
                    AddEditRecipeScreen(
                        recipeId = null,
                        viewModel = addEditRecipeViewModel,
                        navController = navController
                   )

                }

                composable("recipe_edit/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId")?.let { it.toLongOrNull()
                    }

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("my_recipes")
                    }
                    AddEditRecipeScreen(
                        recipeId = recipeId,
                        viewModel = addEditRecipeViewModel,
                        navController = navController
                    )
                }

                composable("select_categories") {
                    SelectCategoriesScreen(
                        navController = navController,
                        viewModel = addEditRecipeViewModel
                    )
                }

                composable("image") {
                    ImageScreen(
                        viewModel = addEditRecipeViewModel,
                        navController = navController
                    )
                }

                composable("ingredients") {
                    IngredientsScreen(
                        viewModel = addEditRecipeViewModel,
                        navController = navController
                    )
                }

                composable("steps") {
                    StepScreen(
                        viewModel = addEditRecipeViewModel,
                        navController = navController
                    )
                }

                composable("search_ingredients") {
                    IngredientsForSearchScreen(
                        ingredientsViewModel = searchByIngredientsViewModel,
                        recipesViewModel = recipeViewModel,
                        navController = navController
                    )
                }

                composable("search_result") {
                    SearchResultScreen(
                        searchByIngredientsViewModel, recipeViewModel, navController
                    )
                }

                composable("admin") {
                    AdminScreen(
                        adminViewModel, authViewModel, navController
                    )
                }

                composable("admin_ingredient") {
                    IngredientAdminScreen(authViewModel, recipeViewModel, adminViewModel, navController)
                }

                composable("admin_category") {
                    CategoryAdminScreen(authViewModel, recipeViewModel, adminViewModel, navController)
                }

                composable("admin_change_categoryvalue/{id}/{typeId}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                    var typeId = backStackEntry.arguments?.getString("typeId")?.toLongOrNull()
                    if (typeId == null) typeId = 1L

                    AddEditCategoryScreen(
                        id,
                        typeId,
                        false,
                        recipeViewModel,
                        authViewModel,
                        adminViewModel,
                        navController)
                }

                composable("admin_change_categoryType") {
                    AddEditCategoryTypeScreen(
                        recipeViewModel,
                        authViewModel,
                        adminViewModel,
                        navController
                    )
                }

                composable("admin_new_edit_categoryType/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()

                    Log.d("ADMIN", "NavGrapf: id = $id")

                    NewUpdateCategoryTypeScreen(
                        id,
                        authViewModel,
                        adminViewModel,
                        navController
                    )
                }

                composable("admin_add_ingredient") {
                    AddEditIngredientScreen(
                        ingredientId = null,
                        adminViewModel = adminViewModel,
                        authViewModel = authViewModel,
                        navController = navController,
//                        isEdit = false,
//                        onSave = {}
                    )
                }

                composable("admin_edit_ingredient/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()


                    Log.d("ADMIN", "NavGraph: id = $id")

                    AddEditIngredientScreen(
                        ingredientId = id,
                        adminViewModel = adminViewModel,
                        authViewModel = authViewModel,
                        navController = navController,
//                        isEdit = false,
//                        onSave = {}
                    )
                }



            }       //  NavHost
}