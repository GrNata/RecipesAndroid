package com.grig.recipesandroid.ui.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    Log.d("CICLE NAV_TRACE", "LoginScreen НАЧАЛО")

    val loading by authViewModel.loading.collectAsState()
    val error by authViewModel.error.collectAsState()
    val tokens by authViewModel.tokens.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginConsumed by remember { mutableStateOf(false) }

    // Если логин успешен
    LaunchedEffect(tokens) {
        Log.d("CICLE NAV_TRACE", "LoginScreen LaunchedEffect(tokens): $loginConsumed, token: $tokens")
//        if (tokens != null) onLoginSuccess()
        if (tokens != null && !loginConsumed) {
            loginConsumed = true
//            onLoginSuccess()
            //    для -  «возврата на экран с которого повторное логирование»
                val route = authViewModel.consumePendingRoute()

                if (route != null) {
                    navController.navigate(route) {
                        popUpTo("login") { inclusive = true  }
                    }
                }
                else {
                    navController.navigate("recipe_list") {
                        popUpTo("login") { inclusive = true  }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Log.d("CICLE NAV_TRACE", "LoginScreen Column")
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { authViewModel.login(email, password) },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Login")
        }
        error?.let{
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
//        TextButton(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                "Нет аккаунта? Зарегистрироваться.",
                color = MaterialTheme.colorScheme.surface,
            )
        }
    }
}