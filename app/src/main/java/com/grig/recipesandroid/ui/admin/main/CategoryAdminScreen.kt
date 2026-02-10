package com.grig.recipesandroid.ui.admin.main

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.CategoryTypeDropDown

@Composable
fun CategoryAdminScreen(
    authViewModel: AuthViewModel,
    recipesViewModel: RecipesViewModel,
    adminViewModel: AdminViewModel,
    navController: NavController
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val categoryTypesAll = recipesViewModel.categoryTypesAll
    val categoryValuesAll = recipesViewModel.categoryValuesAll

    var selectedCategoryTypeId by remember { mutableStateOf(1L) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

//        //        Кнопка добавить categoryValue
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    adminViewModel.resetFormCategoryValue()
//                    navController.navigate("admin_change_categoryvalue/{id}/{typeId}")
//                },
//                containerColor = MaterialTheme.colorScheme.onTertiary,
//                modifier = Modifier.size(35.dp)
//            ) {
//                Icon(
//                    Icons.Default.Add,
//                    contentDescription = "Добавить  categoryValue",
//                    tint = Color(0xFFFFFFFF)
//                )
//            }
//        },
//        floatingActionButtonPosition = FabPosition.End, // не обязательно, но правильно

        topBar = {
            AdminAppTopBar(
                title = "Категории - АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                onIngredientAdmin = { navController.navigate("admin_ingredient") },
//                onCategoryAdmin = { navController.navigate("admin_category") },
                onAuditLogs = { navController.navigate("admin_audit_logs") },
                onStatistics = { navController.navigate("admin_statistics") },
                onUsers =  { navController.navigate("admin") },
    )
}

) { paddingValues ->

        Log.d("ADMIN", "CategoryAdminScreen: categoryValuesAll: ${categoryValuesAll}")

//        Box(
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF7EDE9)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.padding(top = 32.dp, start = 16.dp)
            ) {
                // Dropdown для выбора группировки
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(3f)) {
                        CategoryTypeDropDown(
                            categoryTypes = categoryTypesAll,
                            selectedId = selectedCategoryTypeId,
                            onSelected = { selectedCategoryTypeId = it }
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        IconButton(
                            onClick = { navController.navigate("admin_change_categoryType")},
                            modifier = Modifier.padding(12.dp),
                            colors = IconButtonColors(
                                containerColor = Color(0xFFD2C2C7),
//                                containerColor = Color(0xFF628AB4),
                                contentColor = Color(0xFF3C326B),
//                                contentColor = Color(0xFF062444),
                                disabledContentColor =  Color(0xFF6F6AB8),
                                disabledContainerColor =  Color(0xFFD0A769)
                                )
//                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Sharp.Edit,
                                contentDescription = "Редактировать тип категорий",
                                modifier = Modifier.size(30.dp).padding(end = 8.dp),
//                                tint = Color(0xFF123C69)
                            )
                        }
                    }

                }

                val groupedCategory = GroupedCategoryValueByCategoryType(selectedCategoryTypeId, categoryValuesAll)

                Log.d("ADMIN", "CategoryAdminScreen: selectedCategoryTypeId: $selectedCategoryTypeId")
//                Log.d("ADMIN", "CategoryAdminScreen: groupedCategory: $groupedCategory")

                Box(
//                    modifier = Modifier.padding(16.dp),

                ) {
//                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .background(Color(0xFFFFFBFB))
                        .border(
                            border = BorderStroke(1.dp, Color(0xFF9D9598))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                    items(categoryValuesAll) { category ->
                        items(groupedCategory) { category ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(
                                        "${category.categoryValue}",
                                        color = Color(0xFF3C326B),
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }


                                Column(modifier = Modifier.weight(0.3f)) {
                                    IconButton(
                                        onClick = { navController.navigate("admin_change_categoryvalue/${category.id}/${selectedCategoryTypeId}") },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Редактировать ингредиент",
                                            tint = Color(0xFF3C326B)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.weight(0.3f)) {
                                    IconButton(
                                        onClick = { adminViewModel.deleteCategoryValue(category.id) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Удалить ингредиент",
                                            tint = Color(0xFF3C326B)
                                        )
                                    }
                                }
                            }   // Row
                            Divider(
                                color = Color(0xFF9D9598),
                                thickness = 1.dp
                            )
                        }
                    }   //  LazyColumn
                }   //  Box
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    //        Кнопка добавить categoryValue
//            floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            adminViewModel.resetFormCategoryValue()
                            navController.navigate("admin_change_categoryvalue/{id}/{typeId}")
                        },
                        containerColor = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Добавить  categoryValue",
                            tint = Color(0xFFFFFFFF)
                        )
                    }
//            }
//            floatingActionButtonPosition = FabPosition.End, // не обязательно, но правильно
                }
            }
        }

    }
}