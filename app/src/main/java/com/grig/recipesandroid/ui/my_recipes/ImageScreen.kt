package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar

@Composable
fun ImageScreen(
    viewModel: AddEditRecipeViewModel,
    navController: NavController
) {

    Scaffold(

        topBar = {
            AppTopBar(
                title = "Картинка",
                isAuthenticated = true,
                showMyRecipes = true,
                onBack = { navController.popBackStack() },
                onLoginClick = {},
                onLogoutClick = {},
//                authViewModel = viewModel.authViewModel
//                authViewModel = addEditViewMode,.authViewModel
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Log.d("AddEdit-image", "ImageScreen: start")
            Log.d("AddEdit-image", "ImageScreen: image = ${viewModel.image}")

            val image = viewModel.image.orEmpty()

                TextField(
                    value = image,
                    onValueChange = viewModel::onImageChange,
                    label = { Text("Фото (url)") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Картинка под полем ввода
                AsyncImage(  // требуется библиотека Coil
                    model = image,
                    contentDescription = "Загруженное фото",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Fit
                )
        }
    }

}