package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun CategoryAdminScreen(
    authViewModel: AuthViewModel,
    recipesViewModel: RecipesViewModel,
    navController: NavController
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val categoryTypesAll = recipesViewModel.categoryTypesAll
    val categoryValuesAll = recipesViewModel.categoryValuesAll

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

        Log.d("ADMIN", "CategoryAdminScreen: categoryValuesAll: ${categoryValuesAll}")

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(categoryValuesAll) { category ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Text("${category.typeName}: ${category.categoryValue}")
                }
            }
        }

    }
}