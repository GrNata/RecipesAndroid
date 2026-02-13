package com.grig.recipesandroid.ui.admin.topBarAdmin

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.grig.recipesandroid.data.model.AdminMenuItem

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

//    isCategory: Boolean? = false,
//    isIngredient: Boolean? = null,
    navController: NavController
) {
    var menuExpanded by remember { mutableStateOf(false) }

//    val isModerator by authViewModel.isModerator.collectAsState()


    val menuItem = listOf(
        AdminMenuItem("Пользователи") { navController.navigate("admin") },
        AdminMenuItem("Ингредиенты") { navController.navigate("admin_ingredient") },
        AdminMenuItem("Категории") { navController.navigate("admin_category") },
        AdminMenuItem("Статистика") { navController.navigate("admin_statistics") },
        AdminMenuItem("Графики-статистика") { navController.navigate("admin_stats_graph") },
        AdminMenuItem("Аудит-логи") { navController.navigate("admin_audit_logs") }
    )

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
                    menuItem.forEach { item ->
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                },
                                text = { Text(item.title) },
                                onClick = {
                                    menuExpanded = false
                                    item.onClick()
                                }
                            )
                    }
                }
            }

        }
    )
}