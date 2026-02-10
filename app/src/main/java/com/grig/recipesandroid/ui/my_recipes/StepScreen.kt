package com.grig.recipesandroid.ui.my_recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@Composable
fun StepScreen(
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {

    Scaffold(

        topBar = {
            AppTopBar(
                title = "Шаги приготовления",
                isAuthenticated = true,
                showMyRecipes = true,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                onSearchByIngredients = {}
//                authViewModel = viewModel.authViewModel
//                authViewModel = addEditViewMode,.authViewModel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // --- динамический список шагов приготовления ---
            StepsWithDinamicList(viewModel)
        }
    }
}