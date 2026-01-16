package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RecipeListTopBar(
//    title: String = "Рецепты",
//    isAuthenticated: Boolean,
//    onLoginClick: () -> Unit,
//    onLogoutClick: () -> Unit,
//    onBack: ( () -> Unit)? = null,      // кнопка Назад, optional
//    onShareClick: (() -> Unit)? = null      // для деталей, optional - ???
//) {
//    TopAppBar(
//        title = { Text(title) },
//        navigationIcon = if (onBack != null) {
//            {
//                IconButton(onClick = onBack) {
//                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
//                }
//            }
//        } else null,
//        actions = {
//            if (onShareClick != null) {
//                IconButton(onClick = onShareClick) {
//                    Icon(Icons.Default.Share, contentDescription = "Поделиться")
//                }
//            }
//
//            if (isAuthenticated) {
//                IconButton(onClick = onLogoutClick) {
//                    Icon(Icons.Default.AccountBox, contentDescription = "Выйти")
//                }
//            } else {
//                TextButton(onClick = onLoginClick) {
//                    Text("Войти")
//                }
//            }
//        }
//    )
//}