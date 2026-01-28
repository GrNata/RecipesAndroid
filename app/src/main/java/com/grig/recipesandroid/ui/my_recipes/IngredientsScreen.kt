package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@Composable
fun IngredientsScreen(
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {

    Scaffold(

        topBar = {
            AppTopBar(
                title = "Картинка",
                isAuthenticated = true,
                showMyRecipes = true,
                onBack = {
                    viewModel.cleanEmptyIngredients()
                    navController.popBackStack()
                         },
                onLoginClick = {},
                onLogoutClick = {},
//                Сделать !!!
                onSearchByIngredients = {}
            )
        }
    ) { paddingValues ->

        Log.d("AddEdit-ingredient", "IngredientsScreen: ingredients: ${viewModel.ingredients}")

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFEFEFEF))
        ) {

            val readyIngredients = viewModel.ingredients

            Log.d("Calories", "IngredientsWithDinamicList: readyIngredients: ${readyIngredients}")

            viewModel.calculationCalories()

            Row() {
                Text(
                    text = " Калории:"
                )
                Text(
                    text = viewModel.totalCalories.toString(),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }


            // --- динамический список ингредиентов ---
            IngredientsWithDinamicList(viewModel)
        }
    }
}