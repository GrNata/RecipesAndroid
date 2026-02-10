package com.grig.recipesandroid.ui.admin.topBarAdmin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAppTopBar(
    title: String,
    isAuthenticated: Boolean,
    isAdmin: Boolean = false,
//    showMyRecipes: Boolean,
    onMainScreen: (() -> Unit),
    onBack: (() -> Unit),
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
//    onShareClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
//    onMyRecipesClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
//    onSearchByIngredients: (() -> Unit)? = null,
//    onAdmin:(() -> Unit)? = null,
    onIngredientAdmin:(() -> Unit)? = null,
    onCategoryAdmin:(() -> Unit)? = null,
    onAuditLogs: (() -> Unit)? = null,
    onStatistics: (() -> Unit)? = null,
    onUsers: (() -> Unit)? = null,
//    isCategory: Boolean? = false,
//    isIngredient: Boolean? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(
            title,
            maxLines = 1, // Заголовок в одну строку
            overflow = TextOverflow.Ellipsis // Если не влезет — будет троеточие
        ) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color(0xFF883F58),
            navigationIconContentColor = Color(0xFf3C254E),
            actionIconContentColor = Color(0xFf59595C)
        ),
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        },
        actions = {

            IconButton(onClick = onMainScreen) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "К списку рецептов - главный экран",
                    tint = MaterialTheme.colorScheme.surface
                )
            }

            if (isAdmin) {
                //            Кнопка
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Меню",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
//            Раскрывающееся меню
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    if (onUsers != null) {
                        DropdownMenuItem(
                            text = { Text("Пользователи") },
                            onClick = {
                                menuExpanded = false
                                onUsers()
                            }
                        )
                    }
                    if (onIngredientAdmin != null) {
                        DropdownMenuItem(
                            text = { Text("Ингредиенты") },
                            onClick = {
                                menuExpanded = false
                                onIngredientAdmin()
                            }
                        )
                    }
                    if (onCategoryAdmin != null) {
                        DropdownMenuItem(
                            text = { Text("Категории") },
                            onClick = {
                                menuExpanded = false
                                onCategoryAdmin()
                            }
                        )
                    }
                    if (onStatistics != null) {
                        DropdownMenuItem(
                            text = { Text("Статистика") },
                            onClick = {
                                menuExpanded = false
                                onStatistics()
                            }
                        )
                    }
                    if (onAuditLogs != null) {
                        DropdownMenuItem(
                            text = { Text("Аудит-логи") },
                            onClick = {
                                menuExpanded = false
                                onAuditLogs()
                            }
                        )
                    }
                }
            }

        }
    )
}