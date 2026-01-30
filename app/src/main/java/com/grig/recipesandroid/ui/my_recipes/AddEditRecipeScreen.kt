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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.utilRecipe.LabledTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRecipeScreen(
    recipeId: Long?,
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Log.d("errorMessage", "START")
            snackbarHostState.showSnackbar(it)
            Log.d("errorMessage", "END SHOW")
            viewModel.clearError()
        }
    }

    val scrollState = rememberScrollState()

    val isEdit = recipeId != null

    LaunchedEffect(recipeId) {
        viewModel.loadCategoryValues()  // сначала загружаем все категории
        viewModel.loadIngredientAndUnitDictionaries()
        viewModel.loadCategoryTypes()

        if (recipeId != null) {
            // редактирование
            viewModel.loadRecipe(recipeId)
        }
    }

    Log.d("Calories", "AddEditRecipeScreen: ingredientsAll (словарь): ${viewModel.ingredientsAll}")
    Log.d("Calories", "AddEditRecipeScreen: ingredientsAll size: ${viewModel.ingredientsAll.size}")

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        topBar = {
            AppTopBar(
                title = if (isEdit) "Редактировать рецепт" else "Добавить рецепт",
                isAuthenticated = true,
                showMyRecipes = true,
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                onSearchByIngredients = {  }
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

            LabledTextField(
                label = "Название",
                value = viewModel.name,
                onValueChange = viewModel::onNameChange,
                isError = viewModel.name.isBlank()
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

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.baseServings?.toString() ?: "1",
                onValueChange = viewModel::onBaseServings,
                label = { Text("Количество порций * ")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                isError = viewModel.baseServings == null,
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
                Text("Добавить картинку рецепта")
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
                Text("Добавить ингредиенты")
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
                Text("Добавить шаги приготовления")
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
                    viewModel.onRecipeSave(
                        isEdit = isEdit,
                        onSuccess = {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("REFRESH_RECIPES", true)
//                            navController.popBackStack()

                            navController.navigate("my_recipes") {
                                popUpTo("recipe_add") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            ) {
                Text(if (isEdit) "Сохранить" else "Создать")
            }
        }
    }
}