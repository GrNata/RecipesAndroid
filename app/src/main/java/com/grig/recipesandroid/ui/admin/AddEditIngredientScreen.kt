package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun AddEditIngredientScreen(
    ingredientId: Long?,
    adminViewModel: AdminViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
//    isEdit: Boolean,
//    onSave: (IngredientAddEdit) -> Unit
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val error by adminViewModel.error.collectAsState()

    val isEdit = ingredientId != null

    LaunchedEffect(ingredientId) {

        Log.d("ADMIN", "AddEditIngredinetScreen: ingredientId = $ingredientId")

        if (ingredientId != null) {
            adminViewModel.loadIngredientById(ingredientId)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = if (isEdit) "Редактирование ингредиента" else "Созданеи ингредиента",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onBack = { navController.popBackStack() },
                onMainScreen = { navController.navigate("recipe_list") },
                showMyRecipes = false,
                onLoginClick = {},
                onLogoutClick = {},
                onAdmin = { navController.navigate("admin") },
                onIngredientAdmin = { },
                onCategoryAdmin = { navController.navigate("admin_category")},
                isCategory = false,
                isIngredient = true
//                onAdmin = { navController.navigate("admin")}
            )
        }
    ) {paddingValues ->

        Column (modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.padding(12.dp)
//                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Название: ",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF3E0F41),
                    style = MaterialTheme.typography.bodyLarge
                    )
                TextField(
                    value = adminViewModel.name,
                    onValueChange = adminViewModel::onNameChange,
                    modifier = Modifier.weight(2f),
                    colors = TextFieldDefaults.colors(
                        // Фон поля
                        focusedContainerColor = Color(0xFFFFFBFB),
                        unfocusedContainerColor = Color(0xFFE8DFE2),
                        // Цвет текста
                        focusedTextColor = Color(0xFF3E0F41),
                        unfocusedTextColor = Color(0xFF612F65)
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(12.dp)
//                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Название Eng: ",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF3E0F41),
                    style = MaterialTheme.typography.bodyLarge
                )
                TextField(
                    value = adminViewModel.nameEng ?: "",
                    onValueChange = adminViewModel::onNameEngChange,
                    modifier = Modifier.weight(2f),
                    colors = TextFieldDefaults.colors(
                        // Фон поля
                        focusedContainerColor = Color(0xFFFFFBFB),
                        unfocusedContainerColor = Color(0xFFE8DFE2),
                        // Цвет текста
                        focusedTextColor = Color(0xFF3E0F41),
                        unfocusedTextColor = Color(0xFF612F65)
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(12.dp)
//                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Калории в 100г: ",
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF3E0F41),
                    style = MaterialTheme.typography.bodyLarge
                )
                TextField(
                    value = adminViewModel.energyKcal100g?.toString() ?: "",
                    onValueChange = adminViewModel::onEnergyKcal100Change,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(2f),
                    colors = TextFieldDefaults.colors(
                        // Фон поля
                        focusedContainerColor = Color(0xFFFFFBFB),
                        unfocusedContainerColor = Color(0xFFE8DFE2),
//                        disabledContainerColor = Color(0xBFFF6A00).copy(alpha = 0.5f), // полупрозрачный при отключении

                        // Цвет текста
                        focusedTextColor = Color(0xFF3E0F41),
                        unfocusedTextColor = Color(0xFF612F65),
//                        disabledTextColor = Color.Gray,

//                        // Дополнительные цвета (настройка по желанию)
//                        cursorColor = Color(0xFF123C69),
//                        errorTextColor = Color.Red
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge
                )

            }
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
//                modifier = Modifier.padding(start = 100.dp),
                    onClick = {
                        adminViewModel.saveIngredient(
                            isEdit = isEdit,
                            onSuccess = {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("REFRESH_INGREDIENT", true)
//                            navController.popBackStack()

                                navController.navigate("admin_ingredient") {
                                    popUpTo("admin_add_ingredient") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8E4253),
                        contentColor = Color(0xFFEDE3E5)
                    )
                ) {
                    Text(if (isEdit) "Создать" else "Сохранить")
                }
            }
        }

        if (error != null) {
            Snackbar { Text(error!!) }
        }
    }
}