package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Long?,
    viewModel: AddEditRecipeViewModel = viewModel(),
    navController: NavController
) {

    val isEdit = recipeId != null

//    LaunchedEffect(recipeId) {
//        if (isEdit) {
//            viewModel.loadRecipe(requireNotNull(recipeId))
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()  // сначала загружаем все категории
        if (isEdit && recipeId != null) {
            viewModel.loadRecipe(recipeId)  // потом загружаем рецепт
        }
        if (recipeId == null) {
            viewModel.resetForm()
        }
        viewModel.loadIngredientAndUnitDictionaries()
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

            TextField(
                value = viewModel.description,
                onValueChange = viewModel::onNameChange,
                label = { Text("Описание")}
            )

            CategoriesWIthDropDownMenu(viewModel)

            // --- динамический список ингредиентов ---
            IngredientsWithDinamicList(viewModel)

            // --- динамический список шагов приготовления ---
            Spacer(modifier = Modifier.padding(top = 16.dp))

            StepsWithDinamicList(viewModel)

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