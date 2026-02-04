package com.grig.recipesandroid.ui.admin

import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import okhttp3.internal.wait

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
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (isCategoryType) {
//                category = ""

            } else {
                if (id == null) return@Box

                adminViewModel.loadCategoryValueById(id)

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    // Dropdown для выбора группировки
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CategoryTypeDropDown(
                            categoryTypes = categoryTypesAll,
                            selectedId = selectTypeId,
                            onSelected = { newId ->
                                Log.d("ADMIN", "AddEditCategoryScreen: newId: $newId")
                                selectTypeId = newId
                                Log.d("ADMIN", "AddEditCategoryScreen: newId selectTypeId: $selectTypeId")
                                // Если нужно обновить ViewModel:
//                                adminViewModel.typeIdCategoryValue = newId
                            }
                        )

                    }

//                    получить по id nameType
                    adminViewModel.loadCategoryTypeById(selectTypeId)

//                    val groupedCategory = GroupedCategoryValueByCategoryType(selectedCategoryTypeId, categoryValuesAll)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = adminViewModel.nameCategoryType
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = adminViewModel.nameCategoryValue ?: "",
                            onValueChange = adminViewModel::onNameCategoryValueChange,
                        )
                    }
                }
            }
        }

    }


}