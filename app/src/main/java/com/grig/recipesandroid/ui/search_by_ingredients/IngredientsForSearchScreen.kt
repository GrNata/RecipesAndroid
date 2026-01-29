package com.grig.recipesandroid.ui.search_by_ingredients

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.recipe_list.RecipeItem
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.SearchIngredientChexBox
import com.grig.recipesandroid.ui.utilRecipe.SearchIngredientTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientsForSearchScreen(
    ingredientsViewModel: SearchByIngredientsViewModel,
    recipesViewModel: RecipesViewModel,
    navController: NavController
) {


    // Создаём scaffoldState для SnackBar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

//    val selectedIngredientIds = ingredientsViewModel.selectedIngredientIds
//    val searchRecipes by ingredientsViewModel.searchRecipes

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF7EDE9)),

        ) {

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E4253),
                    contentColor = Color(0xFFEDE3E5)
                ),
                onClick = {
                    ingredientsViewModel.searchRecipesByIngredients()
                    navController.navigate("search_result")
                }
            ) {
                Text("Подобрать рецепты")
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            val ingredients = recipesViewModel.ingredientsDictionary

//            Поиск ингредиентов через CheckBox
//            SearchIngredientChexBox(ingredients, ingredientsViewModel)

            SearchIngredientTextField(ingredients, ingredientsViewModel)

        }

    }
}