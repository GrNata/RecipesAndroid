package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipeItem
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.CategoryTypeDropDown

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyRecipesScreen(
    myViewModul: MyRecipesViewModel,
    recipeViewModel: RecipesViewModel,
    navController: NavController,
    authViewModel: AuthViewModel,
    addEditRecipeViewModel: AddEditRecipeViewModel
) {

    Log.d("СЕРДЦЕ MyRecipesScreen VM_CHECK", " начало MyRecipesScreen, recipeViewModel: $recipeViewModel")  // ← проверьте, что не null

    // 🔹 флаг для отображения LoginScreen
    var showLoginScreen by remember { mutableStateOf(false) }

//    val myRecipes by myViewModul.myRecipes.collectAsState(initial = emptyList())
    val myRecipes = myViewModul.myRecipesPagingFlow.collectAsLazyPagingItems()
    val favorites by recipeViewModel.favorites.collectAsState()

    var selectedCategoryTypeId by remember { mutableStateOf(1L) }
    val categoryTypesAll = recipeViewModel.categoryTypesAll


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

//    // Optional: фильтр по избранным
//    var showOnlyFavorites by remember { mutableStateOf(false) }

    val filteredRecipes = myRecipes.itemSnapshotList.items ?: emptyList()

    Log.d("MY Recipes SIZE", "filteredRecipes size: ${filteredRecipes.size}")
    Log.d("MY Recipes SIZE", "myRecipes size: ${myRecipes.itemSnapshotList.size}")

////    ОБЯЗАТЕЛЬНО добавить защиту в MyRecipesScreen
//    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
//    LaunchedEffect(isAuthenticated) {
//        if (!isAuthenticated) {
//            navController.navigate("recipe_list") {
//                popUpTo("my_recipes") { inclusive = true }
//            }
//        }
//    }

//    группировка по категориям
    val grouped = filteredRecipes
        .flatMap { recipe ->
            recipe.categories
                .filter { it.categoryTypeId == 1L }
                .map { category -> category to recipe }
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

    var isMyRecipes = true

    Scaffold(
        modifier = Modifier.fillMaxSize(),

//        Кнопка добавить рецепт
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("recipe_add")
                },
                containerColor = Color.Red
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить рецепт")
            }
        },
        floatingActionButtonPosition = FabPosition.End, // не обязательно, но правильно

        topBar = {
            AppTopBar(
                title = "Мои рецепты",
                isAuthenticated = true,
                showMyRecipes = false,
                onBack = {
                    isMyRecipes = false
                    navController.popBackStack()
                         },
                onLoginClick = {},
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate("recipe_list") {
                        popUpTo("my_recipes") { inclusive = true }
                        launchSingleTop = true
                    }
                },
//                onShareClick = {
//                    // поделиться только в RecipeItem
//                    Log.d("MY_RECIPES", "Share clicked for my recipes")
//                },
//                authViewModel = authViewModel
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            if (myRecipes.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {

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
                                        text = category,
//                                        text = category.categoryValue,
//                                    text = category.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF123C69)
                                    )
                                }
                            }
//                            }
                            Log.d(
                                "СЕРДЦЕ MyRecipeScreen",
                                "recipesInCategory size ${recipesInCategory.size}"
                            )

                            items(recipesInCategory) { recipe ->
//                            Log.d("MyRecipeItem", "MyRecipe recipe: ${recipe.ingredients.forEach {
//                                (it.unit?.label) ?: ""
//                            }}")

                                RecipeItem(
                                    viewModel = recipeViewModel,
                                    recipe = recipe,
                                    query = "",
                                    isFavorite = favorites.contains(recipe.id),         //  по желанию
                                    isOwner = true,
                                    onFavoriteClick = { recipeViewModel.toggleFavorite(recipe.id) },
                                    onClick = { navController.navigate("recipe_detail/${recipe.id}") },
                                    onEditClick = {
                                        navController.navigate("recipe_edit/${recipe.id}")
                                    },
                                    onDeleteClick = {
//                                    addEditRecipeViewModel.deleteRecipe()
                                    addEditRecipeViewModel.deleteRecipe(recipe.id)
                                    }
                                )
                            }
                        }
                    }           // LazyColumn
                }
            }
        }
    }
}
