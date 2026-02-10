package com.grig.recipesandroid.ui.admin.statistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun AdminStatsScreen(
    adminStatsViewModel: AdminStatsViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()

    val stats by adminStatsViewModel.stats.collectAsState()
    val loading by adminStatsViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        adminStatsViewModel.loadStatistics()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            AdminAppTopBar(
                title = "Статистика",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                onMainScreen = { navController.navigate("recipe_list") },
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
                onIngredientAdmin = { navController.navigate("admin_ingredient") },
                onCategoryAdmin = { navController.navigate("admin_category") },
                onAuditLogs = { navController.navigate("admin_audit_logs") },
//                onStatistics = { navController.navigate("admin_statistics") },
                onUsers =  { navController.navigate("admin") },
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                loading -> CircularProgressIndicator()

                stats != null -> stats?.let { StatsContent(it) }
            }
        }
    }

}