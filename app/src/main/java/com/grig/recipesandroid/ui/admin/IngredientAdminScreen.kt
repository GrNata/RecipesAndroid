package com.grig.recipesandroid.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun IngredientAdminScreen(
    authViewModel: AuthViewModel,
    recipesViewModel: RecipesViewModel,
    adminViewModel: AdminViewModel,
    navController: NavController
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val ingredientAll = recipesViewModel.ingredientsDictionary


//    //    Для добавления вновь созданного ингредиента (обновление списка)
//    val refresh = navController
//        .currentBackStackEntry
//        ?.savedStateHandle
//        ?.getLiveData<Boolean>("REFRESH_INGREDIENT")
////        ?.observeAsState()
//
//    LaunchedEffect(refresh?.value) {
//        if (refresh?.value == true) {
//            adminViewModel.refresh()
//            navController.currentBackStackEntry
//                ?.savedStateHandle
//                ?.remove<Boolean>("REFRESH_RECIPES")
//        }
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        //        Кнопка добавить рецепт
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    adminViewModel.resetFormIngredient()
                    navController.navigate("admin_add_ingredient")
                },
                containerColor = Color.Red,
                modifier = Modifier.size(35.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить ингредиент")
            }
        },
        floatingActionButtonPosition = FabPosition.End, // не обязательно, но правильно

        topBar = {
            AppTopBar(
                title = "Игрединты АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onBack = { navController.popBackStack() },
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
    ) { paddingValues ->

        Box(
            Modifier
                .padding(paddingValues)
                .background(Color(0xFFF7EDE9))
//                .background(Color(0xFFEEE2DC))
        ) {
//            if (LoadState.Loading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 42.dp)
                ) {
                    item {

                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1.2f)) {
                                Text(
                                    "Название \nрус.",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF123C69)
                                )
                            }
                            Column(modifier = Modifier.weight(1.2f)) {
                                Text(
                                    "Название \neng",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF123C69)
                                )
                            }
                            Column(modifier = Modifier.weight(1.2f)) {
                                Text(
                                    "Калории \nв 100г",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF123C69)
                                )
                            }
                        }
                    }

                    items(ingredientAll) { ingredient ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1.5f)) {
                                Text(
                                    "${ingredient.name}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF123C69),
                                    softWrap = true, // Разрешает перенос текста
                                    maxLines = 3,  // Максимум 3 строки (можно убрать ограничение)
                                    overflow = TextOverflow.Ellipsis // Если текст > 3 строк — добавит "..."
                                )
                            }
                            Column(modifier = Modifier.weight(1.5f)) {
                                Text(
                                    "${ingredient.nameEng}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF123C69),
                                    softWrap = true, // Разрешает перенос текста
                                    maxLines = 3,  // Максимум 3 строки (можно убрать ограничение)
                                    overflow = TextOverflow.Ellipsis // Если текст > 3 строк — добавит "..."
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "${ingredient.energyKcal100g}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF123C69)
                                )
                            }
                            Column(modifier = Modifier.weight(0.3f)) {
                                IconButton(
                                    onClick = { navController.navigate("admin_edit_ingredient/${ingredient.id}") },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Редактировать ингредиент",
                                        tint = Color(0xFF123C69)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(0.3f)) {
                                IconButton(
                                    onClick = { adminViewModel.deleteIngredient(ingredient.id) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Удалить ингредиент",
                                        tint = Color(0xFF123C69)
                                    )
                                }
                            }

                        }
                    }
                }   //  LazyColumn
//            }
        }
    }
}