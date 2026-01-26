package com.grig.recipesandroid.ui.search_by_ingredients

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.recipe_list.RecipeItem
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchResultScreen(
    ingredientsViewModel: SearchByIngredientsViewModel,
    recipesViewModel: RecipesViewModel,
    navController: NavController,
//    recipes: List<Recipe>?
) {

    // Получаем данные из SavedStateHandle
    val recipes by ingredientsViewModel.searchRecipes

    Log.d("SEARCH INGREDIENT", "SearchResultScreen: recipes: ${recipes}")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Поиск по ингредиентам",
                isAuthenticated = true,
                showMyRecipes = true,
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues)
        ) {

            val grouped = recipes
                .flatMap { recipe ->
                    recipe.categories
//                        .filter { it.categoryTypeId == selectedCategoryTypeId }
//                                .filter { it.categoryTypeId == 1L }
                        .map { it.categoryValue to recipe }       // создаём пары category -> recipe
                }
                .groupBy(
                    keySelector = { it.first },
                    valueTransform = { it.second }
                )


            LazyColumn(
                modifier = Modifier.fillMaxWidth()
//                modifier = Modifier.fillMaxWidth().padding(paddingValues)
            ) {
                if (!recipes.isEmpty()) {
//                        items(recipes) { recipe ->
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
    //                                    text = category.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF123C69)
                                    )
                                }
                            }

                            items(recipesInCategory) { recipe ->
                                RecipeItem(
                                    viewModel = recipesViewModel,
                                    recipe = recipe,
                                    query = "",
                                    isFavorite = false,
                                    isOwner = false,
                                    onFavoriteClick = {},
                                    onClick = {
                                        navController.navigate("recipe_detail/${recipe.id}")
                                    },
                                    onEditClick = {},
                                    onDeleteClick = { }
                                )
                            }
                        }
                    } else {
                        item {
                            Text("Подходящих рецептов не было")
                        }
                    }
            }       //      LazyColumn
        }
    }
}