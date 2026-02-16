package com.grig.recipesandroid.ui.admin.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
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
    val emailFilter by adminViewModel.emailFilter.collectAsState()

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

//    val queryAdmin by adminViewModel.queryAdmin.collectAsState()

    LaunchedEffect(Unit) {
        adminViewModel.loadUsers()
    }

//    LaunchedEffect(usersAll) {
//        adminViewModel.loadUsers()
//        Log.d("ADMIN", "AdminScreen: LaunchedEffect userAll: ${usersAll}" )
//    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            AdminAppTopBar(
                title = "Пользователи - АДМИН",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {

                //        поиск / фильтрация
                DropDownForFilterUser(adminViewModel)

                if (usersAll.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Ничего не найдено",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(Color(0xFFD3D0D4))
                    ) {
                        items(usersAll) { user ->
                            Column(
                                modifier = Modifier
                            ) {
                                UserRow(user, adminViewModel)
                            }
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