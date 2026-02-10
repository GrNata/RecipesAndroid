package com.grig.recipesandroid.ui.admin.statistic.graph

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.admin.statistic.AdminStatsViewModel
import com.grig.recipesandroid.ui.admin.topBarAdmin.AdminAppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

@Composable
fun GraphStatsScreen(
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
                navController = navController
            )
        }
    ) { paddingValues ->

        Log.d("STATS ADMIN", "GraphStatsScreen: START stats: ${stats}")

        if (loading) {
            CircularProgressIndicator()
            return@Scaffold
        }

        Log.d("STATS ADMIN", "GraphStatsScreen: BEFORE let stats: ${stats}")

        stats?.let {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                StatsSummaryRow(it)

                Spacer(Modifier.height(24.dp))

                CategoryBarChartGraph(it.popularCategoriesValue)

                Spacer(Modifier.height(24.dp))

                if (!it.topAuthors.isNullOrEmpty()) {
                    AuthorBarChartGraph(it.topAuthors)
                } else {
                    Text(
                        "нет данных по авторам",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}