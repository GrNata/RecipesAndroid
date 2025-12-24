package com.grig.recipesandroid

//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavHost
//import androidx.navigation.compose.rememberNavController
//import com.grig.recipesandroid.ui.recipe_list.RecipeListScreen
//import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
//import com.grig.recipesandroid.ui.theme.RecipesAndroidTheme
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.recipesandroid.ui.recipe_list.RecipeListScreenWithNav
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
                            RecipeListScreenWithNav(viewModel = viewModel, navController = navController)
                        }
//                        // composable("recipe_detail/{id}") { ... } можно добавить экран деталей позже
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



