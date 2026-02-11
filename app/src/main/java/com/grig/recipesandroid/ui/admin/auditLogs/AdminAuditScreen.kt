package com.grig.recipesandroid.ui.admin.auditLogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun AdminAuditScreen(
    adminAuditViewModel: AdminAuditViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val logs by adminAuditViewModel.logs.collectAsState()
    val loading by adminAuditViewModel.loading.collectAsState()

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    LaunchedEffect(Unit) {
        adminAuditViewModel.loadLogs()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            AdminAppTopBar(
                title = "Аудит логи",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {

//                AdminAuditFilter(adminAuditViewModel)
                AdminAuditDropDown(adminAuditViewModel)

                LazyColumn() {
                    items(logs) { log ->
                        AuditLogRow(log)
                    }
                }
            }
        }
    }


}