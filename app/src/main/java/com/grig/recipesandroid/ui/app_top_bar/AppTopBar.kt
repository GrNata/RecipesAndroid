package com.grig.recipesandroid.ui.app_top_bar


import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.*
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.colorScheme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isAuthenticated: Boolean,
    isAdmin: Boolean = false,
    showMyRecipes: Boolean,
    onMainScreen: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onShareClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    onMyRecipesClick: (() -> Unit)? = null,      //  nullable — показываем только для залогиненных
    onSearchByIngredients: (() -> Unit)? = null,
    onAdmin:(() -> Unit)? = null,
    onIngredientAdmin:(() -> Unit)? = null,
    onCategoryAdmin:(() -> Unit)? = null,
    isCategory: Boolean? = false,
    isIngredient: Boolean? = null
//    isAdmin: Boolean
//    authViewModel: AuthViewModel
) {

    val tooltipState = remember { TooltipState() }

    TopAppBar(
        modifier = Modifier.height(60.dp),
        title = { Text(
            text = title,
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
            onBack?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        },
        actions = {
//            Кнопка к списку всех рецептов - на главный экран
            if (onMainScreen != null) {
                IconButton(onClick = onMainScreen) {
                    Icon(Icons.Default.Home, contentDescription = "К списку рецептов - главный экран")
                }
            }


            // 1 Кнопка для ADMIN
            Log.d("ADMIN", "AppTopBar: isCategory: $isCategory,  isAdmin = $isAdmin")
            if (isAdmin) {
                if (onAdmin != null) {
//                    Кнопка экран Админа - толко на RecipeListScreen
                    IconButton(onClick = onAdmin) {
                        Icon(Icons.Default.Face, contentDescription = "Admin")
                    }
                }
//                else {
//                    Кнопка для Админа  - ингредиенты
                    if (isIngredient == true && onCategoryAdmin != null) {
                        TooltipBox(
                            tooltip = { Text("Ингредиенты-Админ") },
//                            focusable = true,
//                            modifier = Modifier.clickable(onClick = onCategoryAdmin),
                            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                            state = tooltipState,
//                            enableUserInput = false
                        ) {
                            IconButton(onClick = onCategoryAdmin) {
                                Icon(Icons.Default.Menu, contentDescription = "Ингредиенты")
                            }
                        }

                    }
//                    Кнопка для Админа  - категории
                    if (isCategory == true && onIngredientAdmin != null) {
                        TooltipBox(
                            tooltip = { Text("Категории") },
//                            focusable = true,
                            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                            state = tooltipState
                        ) {
                            IconButton(onClick = onIngredientAdmin) {
                                Icon(Icons.Default.Menu, contentDescription = "Категории-Админ")
                            }
                        }
                    }
//                }
            }

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