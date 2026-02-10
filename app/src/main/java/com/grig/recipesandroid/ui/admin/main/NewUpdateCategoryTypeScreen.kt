package com.grig.recipesandroid.ui.admin.main

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun NewUpdateCategoryTypeScreen(
    id: Long?,
    authViewModel: AuthViewModel,
    adminViewModel: AdminViewModel,
    navController: NavController
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val isEdit = id != null

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AdminAppTopBar(
                title = if(isEdit) "Редактирование типа АДМИН" else "Создание типа АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                navController = navController
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
                if (isEdit) {
                    val type = adminViewModel.loadCategoryTypeById(id)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp, bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isEdit) "Редактирование типа категорий" else "Создание типа категорий",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)

                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFFBFB))
                        .border(
                            border = BorderStroke(1.dp, Color(0xFF9D9598))
                        )
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color(0xFFFBF8F3)),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextField(
                            value = adminViewModel.nameCategoryType,
                            onValueChange = adminViewModel::onNameCategoryTypeChange,
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color(0xFFEFEFEF)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8E4253),
                                contentColor = Color(0xFFEDE3E5)
                            ),
                            onClick = {
                                adminViewModel.saveCategoryType(isEdit, onSuccess = {
                                    navController.previousBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("REFRESH_CATEGORY_TYPE", true)
                                    navController.navigate("admin_change_categoryType") {
                                        popUpTo("admin_category") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                })
                            }
                        ) {
                            Text(if (isEdit) "Сохранить" else "Создать")
                        }
                    }
                }
            }
        }
    }

}