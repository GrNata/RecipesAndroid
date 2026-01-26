package com.grig.recipesandroid.ui.my_recipes

import android.R
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Long?,
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {

    val scrollState = rememberScrollState()

    val isEdit = recipeId != null

//    LaunchedEffect(recipeId) {
//        if (isEdit) {
//            viewModel.loadRecipe(requireNotNull(recipeId))
//        }
//    }

//    LaunchedEffect(Unit) {
    LaunchedEffect(recipeId) {
        viewModel.loadCategoryValues()  // сначала загружаем все категории
        viewModel.loadIngredientAndUnitDictionaries()
        viewModel.loadCategoryTypes()

        if (recipeId != null) {
            // редактирование
            viewModel.loadRecipe(recipeId)
        }
    }
//    LaunchedEffect(recipeId) {
//        Log.d("AddEdit-category", "LaunchedEffect recipeId = $recipeId")
//
//        if (!viewModel.isFormInitialized) {
//
//            viewModel.loadCategoryValues()  // сначала загружаем все категории
//            viewModel.loadIngredientAndUnitDictionaries()
//            viewModel.loadCategoryTypes()
////        viewModel.loadCategories()  // сначала загружаем все категории
//            if (isEdit && recipeId != null) {
//                viewModel.loadRecipe(recipeId)  // потом загружаем рецепт
//            } else {
//                viewModel.resetForm()
//            }
//            viewModel.isFormInitialized = true
//        }
//    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEdit) "Редактировать рецепт" else "Добавить рецепт",
                isAuthenticated = true,
                showMyRecipes = true,
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
                .padding((paddingValues))
                .padding(16.dp)
                .verticalScroll(scrollState)
                .background(Color(0xFFEFEFEF))
        ) {
            Log.d("ADD RECIPE-newEdit", "AddEditRecipeScreen: name: ${viewModel.name}")
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название") },
                colors = TextFieldDefaults.colors(
                    // Фон поля
                    focusedContainerColor = Color( 0xFFF7EDE9),
                    unfocusedContainerColor = Color(0xFFEEE2DC),
                    disabledContainerColor = Color(0xBFFF6A00).copy(alpha = 0.5f), // полупрозрачный при отключении

                    // Цвет текста
                    focusedTextColor = Color(0xFF062444),
                    unfocusedTextColor = Color(0xFF1E364F),
                    disabledTextColor = Color.Gray,

                    // Дополнительные цвета (настройка по желанию)
                    cursorColor = Color(0xFF123C69),
                    errorTextColor = Color.Red
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Описание")},
                colors = TextFieldDefaults.colors(
                    // Фон поля
                    focusedContainerColor = Color(0xFFF7EDE9),
                    unfocusedContainerColor = Color(0xFFEEE2DC),
                    disabledContainerColor = Color(0xBFFF6A00).copy(alpha = 0.5f), // полупрозрачный при отключении

                    // Цвет текста
                    focusedTextColor = Color(0xFF062444),
                    unfocusedTextColor = Color(0xFF123C69),
                    disabledTextColor = Color.Gray,

                    // Дополнительные цвета (настройка по желанию)
                    cursorColor = Color(0xFF123C69),
                    errorTextColor = Color.Red
                ),
                textStyle = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

//            ++++++++++
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEE2DC),
                    contentColor = Color(0xFF123C69)
                ),
                onClick = {
                    navController.navigate("image")
                }
            ) {
                Text("Картинка рецепта")
            }
//      ++++++++++++++++++++++++++

            Spacer(modifier = Modifier.padding(top = 16.dp))


//            CategoriesWIthDropDownMenu(viewModel)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEE2DC),
                contentColor = Color(0xFF123C69)
            ),
                onClick = {
                    navController.navigate("select_categories")
                }
            ) {
                Text("Выбрать категорию рецепта")
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // --- динамический список ингредиентов ---
//            IngredientsWithDinamicList(viewModel)
            Button(
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEE2DC),
                    contentColor = Color(0xFF123C69)
                ),
                onClick = {
                    navController.navigate("ingredients")
                }
            ) {
                Text("Ингредиенты")
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // --- динамический список шагов приготовления ---
            Button(
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEE2DC),
                    contentColor = Color(0xFF123C69)
                ),
                onClick = {
                    navController.navigate("steps")
                }
            ) {
                Text("Шаги приготовления")
            }
//            StepsWithDinamicList(viewModel)

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Button(
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xFFEFEFEF)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E4253),
                    contentColor = Color(0xFFEDE3E5)
                ),
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