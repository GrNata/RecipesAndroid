package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
        topBar = {
            AppTopBar(
                title = "Категории АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onBack = { navController.popBackStack() },
                showMyRecipes = false,
                onLoginClick = {},
                onLogoutClick = {},
                onAdmin = { navController.navigate("admin") },
                onIngredientAdmin = { navController.navigate("admin_ingredient")},
                onCategoryAdmin = { },
                isCategory = true,
                isIngredient = false
//                onAdmin = { navController.navigate("admin")}
            )
        }
    ) { paddingValues ->

        Log.d("ADMIN", "CategoryAdminScreen: categoryValuesAll: ${categoryValuesAll}")

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFF7EDE9))
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                // Dropdown для выбора группировки
                Row(modifier = Modifier.fillMaxWidth()) {
                    CategoryTypeDropDown(
                        categoryTypes = categoryTypesAll,
                        selectedId = selectedCategoryTypeId,
                        onSelected = { selectedCategoryTypeId = it }
                    )

                    IconButton(
                        onClick = { navController.navigate("admin_change_category/${selectedCategoryTypeId}") },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Редактировать тип категории",
                            tint = Color(0xFF123C69)
                        )
                    }
                }

                val groupedCategory = GroupedCategoryValueByCategoryType(selectedCategoryTypeId, categoryValuesAll)

                Log.d("ADMIN", "CategoryAdminScreen: selectedCategoryTypeId: $selectedCategoryTypeId")
//                Log.d("ADMIN", "CategoryAdminScreen: groupedCategory: $groupedCategory")

                LazyColumn(modifier = Modifier.padding(paddingValues)) {
//                    items(categoryValuesAll) { category ->
                    items(groupedCategory) { category ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        ) {
                            Column() {
                                Text("${category.typeName}: ${category.categoryValue}")
                            }

                            Column(modifier = Modifier.weight(0.3f)) {
                                IconButton(
                                    onClick = { navController.navigate("admin_change_categoryvalue/${category.id}/${selectedCategoryTypeId}") },
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
                                    onClick = { adminViewModel.deleteCategoryValue(category.id) },
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
            }
        }

    }
}