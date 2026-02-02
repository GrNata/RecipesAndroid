package com.grig.recipesandroid.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun AdminScreen(
    adminViewModel: AdminViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val usersAll by adminViewModel.usersAll.collectAsState()
    val loading by adminViewModel.loading.collectAsState()
    val error by adminViewModel.error.collectAsState()

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        adminViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "АДМИН",
                isAuthenticated = isAuthenticated,
                onBack = { navController.popBackStack() },
                showMyRecipes = false,
                onLoginClick = {},
                onLogoutClick = {},
//                onAdmin = { navController.navigate("admin")}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn() {
                    items(usersAll) { user ->
                        UserRow(user, adminViewModel::updateRole)
//                        UserRow(user, adminViewModel::updateRole)
                    }
                }
            }
            error?.let {
                Text(
                    text = it,
                    color = Color.Red
                )
            }
        }
    }
}