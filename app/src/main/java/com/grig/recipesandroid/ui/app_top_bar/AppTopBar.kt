package com.grig.recipesandroid.ui.app_top_bar

import android.graphics.drawable.Icon
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
import androidx.compose.ui.text.input.KeyboardType.Companion.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isAuthenticated: Boolean,
    onBack: (() -> Unit)? = null,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onShareClick: (() -> Unit)? = null      //  nullable — показываем только для залогиненных
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        },
        actions = {
            // 1️⃣ Кнопка Share (если есть рецепт и пользователь залогинен)
            if (isAuthenticated && onShareClick != null) {
                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "Поделиться рецептом")
                }
            }

            // 2️⃣ Кнопка Войти / Выйти
            if (isAuthenticated) {
                IconButton(onClick = onLogoutClick) {
//                    Icon(Icons.Default.Logout, contentDescription = "Выйти")
                    Icon(Icons.Default.AccountBox, contentDescription = "Выйти")
                }
            } else {
                TextButton(onClick = onLoginClick) {
                    Text("Войти")
                }
            }
        }
    )
}