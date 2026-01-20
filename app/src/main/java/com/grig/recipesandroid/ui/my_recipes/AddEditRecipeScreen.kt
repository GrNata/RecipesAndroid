package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun AddrecipeEditRecipeScreen(
    recipeId: Long?,
    viewModel: AddEditRecipeViewModel = viewModel(),
    navController: NavController
) {

    val isEdit = recipeId != null

    LaunchedEffect(recipeId) {
        if (isEdit) {
            viewModel.loadRecipe(requireNotNull(recipeId))
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEdit) "Редактировать рецепт" else "Добавить рецепт",
                isAuthenticated = true,
                showMyRecipes = true,
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
//                authViewModel = viewModel.authViewModel
//                authViewModel = addEditViewMode,.authViewModel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding((paddingValues))
                .padding(16.dp)
        ) {
            TextField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название")}
            )

            Log.e("CICLE NAV_TRACE", "AddEditRecipeScreen navigate to login from RecipeDetailScreen")
            Button(
                onClick = {
                    if (isEdit) viewModel.updateRecipe()
                    else viewModel.createRecipe()

                    navController.popBackStack()
                }
            ) {
                Text(if (isEdit) "Сохранить" else "Создать")
            }
        }
    }
}