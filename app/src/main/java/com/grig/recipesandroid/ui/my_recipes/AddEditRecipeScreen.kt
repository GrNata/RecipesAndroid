package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Long?,
    viewModel: AddEditRecipeViewModel = viewModel(),
    navController: NavController
) {

    val scrollState = rememberScrollState()

    val isEdit = recipeId != null

//    LaunchedEffect(recipeId) {
//        if (isEdit) {
//            viewModel.loadRecipe(requireNotNull(recipeId))
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.loadCategoryValues()  // сначала загружаем все категории
//        viewModel.loadCategories()  // сначала загружаем все категории
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
                .verticalScroll(scrollState)
        ) {
            TextField(
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название")}
            )

            TextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Описание")}
            )

            if (viewModel.image != null) {
                TextField(
                    value = viewModel.image!!,  // гарантированно не null внутри блока
                    onValueChange = viewModel::onImageChange,
                    label = { Text("Фото (url)") }
                )
                // Картинка под полем ввода
                AsyncImage(  // требуется библиотека Coil
                    model = requireNotNull(viewModel.image),
                    contentDescription = "Загруженное фото",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                TextField(
                    value = "",
                    onValueChange = viewModel::onImageChange,
                    label = { Text("Фото (url)") }
                )
            }

            CategoriesWIthDropDownMenu(viewModel)

            // --- динамический список ингредиентов ---
            IngredientsWithDinamicList(viewModel)

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // --- динамический список шагов приготовления ---
            StepsWithDinamicList(viewModel)

            Button(
                onClick = {
                    if (isEdit) viewModel.updateRecipe(onSuccess = {
                        // После успешного редактирования
                        // Можно уведомить предыдущий экран, что данные изменились
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("REFRESH_RECIPES", true)

                        // И вернуться назад
                        navController.popBackStack()
                    })
                    else viewModel.createRecipe {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("REFRESH_RECIPES", true)
                        navController.popBackStack()
                    }
                }
            ) {
                Text(if (isEdit) "Сохранить" else "Создать")
            }
        }
    }
}