package com.grig.recipesandroid.ui.moderator

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

@Composable
fun ModeratorScreen(
    resipViewModel: RecipesViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val pending by resipViewModel.pendingRecipes.collectAsState()
    val loading by resipViewModel.moderatorLoading.collectAsState()
    val error by resipViewModel.moderatorError.collectAsState()

    val isModerator by authViewModel.isModerator.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        resipViewModel.loadPendingRecipes()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
//        containerColor = Color(0xFFF7F3EC),
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            val authRestored by authViewModel.authStateRestored.collectAsState()
            Log.d("MODERATOR", "ModeratorScreen: authRestored: $authRestored")

            AppTopBar(
                title = "Модератор",
                isAuthenticated = isAuthenticated,
                isModerator = isModerator,
                showMyRecipes = false,
                onBack = { navController.popBackStack() },
                onLoginClick = { navController.navigate("login") },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onModerator = { navController.navigate("moderator") }
            )
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            error ?: "",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                pending?.content.isNullOrEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Нет рецептов на проверке",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                else -> pending?.let { nonNullPending ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(Color(0xFFD3D0D4))
                    ) {

                        items(nonNullPending.content) { recipe ->

                            Text(
                                text = recipe.name
                            )
                            Text(
                                text = recipe.status.toString()
                            )
                        }
                    }   //  LazyColumn
                }   //  else
            }   //  when
        }

    }
}

