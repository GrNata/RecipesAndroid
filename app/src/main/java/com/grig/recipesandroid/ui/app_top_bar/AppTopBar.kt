package com.grig.recipesandroid.ui.app_top_bar

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import com.grig.recipesandroid.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isAuthenticated: Boolean,
    onBack: (() -> Unit)? = null,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onShareClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    onMyRecipesClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    authViewModel: AuthViewModel
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
            // 1 Кнопка Share (если есть рецепт и пользователь залогинен)
            if (isAuthenticated && onShareClick != null) {
                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "Поделиться рецептом")
                }
            }

            // 2 Кнопка "Мои рецепты" (только для залогиненных и если передан обработчик)
            val authRestored by authViewModel.authStateRestored.collectAsState()
            Log.d("NAV MyRecipe", "authRestored: $authRestored")
            if (authRestored && isAuthenticated && onMyRecipesClick != null) {
                IconButton(onClick = onMyRecipesClick) {
                    Icon(Icons.Default.List, contentDescription = "Мои рецепты")
                }
            }

            // 3 Кнопка Войти / Выйти
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