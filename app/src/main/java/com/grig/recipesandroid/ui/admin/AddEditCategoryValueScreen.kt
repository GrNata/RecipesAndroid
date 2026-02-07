package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.CategoryTypeDropDown

@Composable
fun AddEditCategoryScreen(
    id: Long?,
    typeId: Long,
    isCategoryType: Boolean,
    recipesViewModel: RecipesViewModel,
    authViewModel: AuthViewModel,
    adminViewModel: AdminViewModel,
    navController: NavController
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val categoryTypesAll = recipesViewModel.categoryTypesAll
    val categoryValuesAll = recipesViewModel.categoryValuesAll

    var selectTypeId: Long by remember { mutableStateOf(typeId) }

    adminViewModel.resetFormCategoryValue()

    val isEdit = id == null


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = "Названия категорий АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onBack = { navController.popBackStack() },
                onMainScreen = { navController.navigate("recipe_list") },
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
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (isCategoryType) {
//                category = ""

            } else {
//                if (id == null) return@Box

                if (id != null) {
                    adminViewModel.loadCategoryValueById(id)
                }
//                else {
//                    adminViewModel.resetFormCategoryValue()
//                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    // Dropdown для выбора типа категории
                    Row(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
                        CategoryTypeDropDown(
                            categoryTypes = categoryTypesAll,
                            selectedId = selectTypeId,
                            onSelected = { newId ->
                                Log.d("ADMIN", "AddEditCategoryScreen: newId: $newId")
                                selectTypeId = newId
                                Log.d(
                                    "ADMIN",
                                    "AddEditCategoryScreen: newId selectTypeId: $selectTypeId"
                                )
                                // Если нужно обновить ViewModel:
//                                adminViewModel.typeIdCategoryValue = newId
                            }
                        )

                    }

//                    получить по id nameType
                    adminViewModel.loadCategoryTypeById(selectTypeId)

//                    val groupedCategory = GroupedCategoryValueByCategoryType(selectedCategoryTypeId, categoryValuesAll)

                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color(0xFFFFFBFB))
                            .padding(16.dp)
                            .border(
                                border = BorderStroke(1.dp, Color(0xFF9D9598))
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Выбран тип категорий:",
                                color = Color(0xFF3E0F41),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            Arrangement.Center
                        ) {
                            Text(
                                text = adminViewModel.nameCategoryType,
                                color = Color(0xFF612F65),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
//                        val nameType = adminViewModel.nameCategoryType

                        adminViewModel.onNameCategoryTypeByValueChange(adminViewModel.nameCategoryTypeByValue)
                        Log.d(
                            "ADMIN",
                            "AddEditCategoryScreen: Выбран тип категорий: ${adminViewModel.nameCategoryType}"
                        )


                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            "Наименование типа категории:",
                            color = Color(0xFF3E0F41),
                            style = MaterialTheme.typography.titleMedium
                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            Arrangement.Center
                        ) {
                            TextField(
                                value = adminViewModel.nameCategoryValue ?: "",
//                            onValueChange = { adminViewModel.onNameCategoryValueChange(it) },
                                onValueChange = adminViewModel::onNameCategoryValueChange,
                                colors = TextFieldDefaults.colors(
                                    // Фон поля
                                    focusedContainerColor = Color(0xFFFFFBFB),
                                    unfocusedContainerColor = Color(0xFFE8DFE2),
                                    // Цвет текста
                                    focusedTextColor = Color(0xFF3E0F41),
                                    unfocusedTextColor = Color(0xFF612F65),
                                ),
                                textStyle = MaterialTheme.typography.bodyLarge

                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    val nameType = adminViewModel.nameCategoryType

                    Button(
                        modifier = Modifier.padding(start = 100.dp).background(Color(0xFFEFEFEF)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8E4253),
                            contentColor = Color(0xFFEDE3E5)
                        ),
                        onClick = {
                            adminViewModel.saveCategoryValue(
                                typeId = selectTypeId,
                                nameType = nameType,
                                isEdit = isEdit,
                                onSuccess = {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("REFRESH_CATEGORY_VALUE", true)
//                            navController.popBackStack()

                                    navController.navigate("admin_change_categoryvalue/{id}/{$selectTypeId}") {
                                        popUpTo("admin_category") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    ) {
                        Text(if (isEdit) "Создать" else "Сохранить")
                    }
                }
            }
        }

    }


}