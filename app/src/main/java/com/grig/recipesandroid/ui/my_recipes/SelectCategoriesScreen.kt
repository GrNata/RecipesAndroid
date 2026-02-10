package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import kotlinx.coroutines.launch

@Composable
fun SelectCategoriesScreen(
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {
    Log.d("ADD RECIPE-newEdit", "SelectCategoryScreen START-1")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Выбор категорий",
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
        Log.d("ADD RECIPE-newEdit", "SelectCategoryScreen START-2")

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Log.d("AddEdit-category", "SelectCategoriesScreen: start categoryValuesAll size=${viewModel.categoryValuesAll.size}")
            Log.d("AddEdit-category", "SelectCategoriesScreen: start categoryTypesAll size=${viewModel.categoryTypesAll.size}")
            Log.d("AddEdit-category", "SelectCategoriesScreen: selectedCategoryValues = ${viewModel.selectedCategoryValues}")

            viewModel.categoryTypesAll.forEach { type ->

                CategoryRow(
                    typeName = type.nameType,
                    values = viewModel.getCategoryValuesForType(type.id),
                    selected = viewModel.selectedCategoryValues[type.id],
                    onSelect = viewModel::toggleCategoryValueAddEdit
                )
            }
        }
    }

}