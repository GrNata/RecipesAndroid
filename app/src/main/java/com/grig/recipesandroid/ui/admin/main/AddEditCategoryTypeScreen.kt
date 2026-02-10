package com.grig.recipesandroid.ui.admin.main

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun AddEditCategoryTypeScreen(
    recipesViewModel: RecipesViewModel,
    authViewModel: AuthViewModel,
    adminViewModel: AdminViewModel,
    navController: NavController
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val categoryTypesAll = recipesViewModel.categoryTypesAll

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AdminAppTopBar(
                title = "Типы категорий АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color(0xFFF7EDE9))
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Типы категорий",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .background(Color(0xFFFFFBFB))
                        .border(
                            border = BorderStroke(1.dp, Color(0xFF9D9598))
                        )
                ) {
                    items(categoryTypesAll) { type ->
                        Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    "${type.nameType}",
                                    color = Color(0xFF062444)
                                )
                            }
                        }
                            Log.d("ADMIN", "AddEdit: type.id = ${type.id}")
                            Column(modifier = Modifier.weight(1f).size(20.dp)) {
                                IconButton(
                                    onClick = { navController.navigate("admin_new_edit_categoryType/${type.id}") },
//                                    modifier = Modifier.padding(12.dp),
//                                    colors = IconButtonColors(
//                                        containerColor = Color(0xFF628AB4),
//                                        contentColor = Color(0xFF062444),
//                                        disabledContentColor =  Color(0xFF6F6AB8),
//                                        disabledContainerColor =  Color(0xFFD0A769)
//                                    )
//                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Редактировать тип категорий",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(end = 8.dp),
                                        tint = Color(0xFF123C69)
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f).size(20.dp)) {
                                IconButton(
                                    onClick = { adminViewModel.deleteCategoryType(type.id) },
//                                    modifier = Modifier.padding(12.dp),
//                                    colors = IconButtonColors(
//                                        containerColor = Color(0xFF628AB4),
//                                        contentColor = Color(0xFF062444),
//                                        disabledContentColor =  Color(0xFF6F6AB8),
//                                        disabledContainerColor =  Color(0xFFD0A769)
//                                    )
//                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Sharp.Delete,
                                        contentDescription = "Удалить тип категорий",
                                        modifier = Modifier.size(30.dp),
                                        tint = Color(0xFF123C69)
                                    )
                                }
                            }
                        }   //  Row
                        Divider(
                            color = Color(0xFF9D9598),
                            thickness = 1.dp
                        )
                    }
                }   //  LazyColumn


                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(35.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    //        Кнопка добавить categoryType
                    FloatingActionButton(
                        onClick = {
                            adminViewModel.resetFormCategoryType()
                            navController.navigate("admin_new_edit_categoryType/{id}")
                        },
                        containerColor = Color(0xFF8E4253),
                        modifier = Modifier.size(35.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Добавить  categoryType",
                            tint = Color.White
                            )
                    }
                }

            }   //  Column
        }   //  Box

    }
}