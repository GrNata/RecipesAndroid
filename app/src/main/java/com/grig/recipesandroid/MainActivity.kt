package com.grig.recipesandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailScreen
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModel
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModelFactory
import com.grig.recipesandroid.ui.recipe_list.RecipeListScreen
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.theme.RecipesAndroidTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Настройка Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // для эмулятора Android
//            Реальное устройство Android:
//	1.	Узнай IP компьютера в локальной сети, например 192.168.1.100.
//	2.	В baseUrl напиши:
//            .baseUrl("http://192.168.1.100:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(com.grig.recipesandroid.data.api.RecipeApi::class.java)
        val repository = com.grig.recipesandroid.data.repository.RecipeRepository(api)
        val viewModel = RecipesViewModel(repository)

        setContent {
            RecipesAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "recipe_list") {
                        composable("recipe_list") {
                            RecipeListScreen(
                                viewModel = viewModel,
                                navController = navController,
                                onRecipeClick = { recipeId -> navController.navigate("recipe_detail/$recipeId")}
                                )
                        }
                         composable(
                             "recipe_detail/{recipeId}",
                             arguments = listOf(navArgument("recipeId") { type = NavType.LongType })
                             ) { backStackEntry ->
                             val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L
                             val viewModel: RecipeDetailViewModel = viewModel(
                                 factory = RecipeDetailViewModelFactory(api)
                             )
                             RecipeDetailScreen(
                                 recipeId = recipeId,
                                 onBack = { navController.popBackStack() }
                             )
                         }
                    }
                }
            }
        }
    }
}

// Как подключиться к RestApiRecipe на localhost
//
//Если эмулятор Android:
//	•	Используй http://10.0.2.2:8080/ вместо http://localhost:8080/.
//	•	10.0.2.2 — это специальный адрес для эмулятора, который маппится на хост-машину.
//
//Если реальное устройство:
//	•	Найди IP своего ПК в сети (например, 192.168.1.100).
//	•	В baseUrl поставь http://192.168.1.100:8080/.
//	•	Убедись, что PC и телефон в одной сети, и firewall не блокирует порт 8080.



