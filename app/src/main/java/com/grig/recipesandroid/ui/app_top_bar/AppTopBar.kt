package com.grig.recipesandroid.ui.app_top_bar

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isAuthenticated: Boolean,
    showMyRecipes: Boolean,
    onBack: (() -> Unit)? = null,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onShareClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    onMyRecipesClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    onSearchByIngredients: (() -> Unit)? = null
//    authViewModel: AuthViewModel
) {

    TopAppBar(
        title = { Text(
            title,
            color = Color(0xFF8E4253)
        ) },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        },
        actions = {
            // 1 Кнопка поиск рецептов по ингредиентам
            if (onSearchByIngredients != null) {
                IconButton(onClick = onSearchByIngredients) {
                    Icon(Icons.Default.Search, contentDescription = "Поиск рецептов по ингредиентам")
                }
            }

//        actions = {
            // 2 Кнопка Share (если есть рецепт и пользователь залогинен)
            if (isAuthenticated && onShareClick != null) {
                Log.d("SEARCH INGREDIENT", "AppTopBar: onShareClick: ${onShareClick}")
                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "Поделиться рецептом")
                }
            }

//            // 3 Кнопка "Мои рецепты" (только для залогиненных и если передан обработчик)
            if (showMyRecipes && onMyRecipesClick != null) {
                IconButton(onClick = onMyRecipesClick) {
                    Icon(Icons.Default.List, contentDescription = "Мои рецепты")
                }
            }

            // 4 Кнопка Войти / Выйти
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