package com.grig.recipesandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grig.recipesandroid.data.api.AuthApi
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.network.AuthInterceptor
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.auth.LoginScreen
import com.grig.recipesandroid.ui.auth.RegisterScreen
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailScreen
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModel
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModelFactory
import com.grig.recipesandroid.ui.recipe_list.RecipeListScreen
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.theme.RecipesAndroidTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    private lateinit var recipeApi: RecipeApi
    private lateinit var authApi: AuthApi
    private lateinit var tokenRepository: TokenRepository
    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataStore для хранения токенов
        val tokenRepository = TokenRepository(applicationContext)

//        // Настройка Retrofit - Retrofit для API
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:9090/") // для эмулятора Android - сервер RestApiRecipes
//
//            .client(okHttpClient)
////            Реальное устройство Android:
////	1.	Узнай IP компьютера в локальной сети, например 192.168.1.100.
////	2.	В baseUrl напиши:
////            .baseUrl("http://192.168.1.100:8080/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        // 2. AuthApi и RecipeApi через Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // эмулятор Android
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = retrofit.create(AuthApi::class.java)
        val recipeApiTemp = retrofit.create(RecipeApi::class.java)

        // 3. AuthRepository
        authRepository = AuthRepository(authApi, tokenRepository)

        // 4. OkHttpClient с AuthInterceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository, authRepository))
            .build()

        // 5. RecipeApi с клиентом, который знает про токены
        recipeApi = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)

        // 6. Repositories
        val recipeRepository = RecipeRepository(recipeApi)
        val favoritesRepository = FavoritesRepository(recipeApi, tokenRepository)

        // 7. Compose и NavHost
        setContent {
            RecipesAndroidTheme {
                val navController = rememberNavController()

                Log.e("ИЩУ:", "MainActivity: NavHost создаётся, startDestination=recipe_list")

                // Создаём ViewModels
                val authViewModel: AuthViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return AuthViewModel(
                            authRepository,
                            tokenRepository
                            ) as T
                    }
                })

                val recipesViewModel: RecipesViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return RecipesViewModel(recipeRepository, favoritesRepository) as T
                    }
                })

//            RecipesAndroidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    NavHost(
                        navController = navController,
//                        startDestination = "login"
                        startDestination = "recipe_list"
                    ) {
                        // Login screen
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = { navController.navigate("recipe_list") }
                            )
                        }

                        // Register screen
                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = { navController.navigate("recipe_list") }
                            )
                        }

                        // Recipe list screen
                        composable("recipe_list") {
                            RecipeListScreen(
                                viewModel = recipesViewModel,
                                navController = navController,
                                onRecipeClick = { recipeId ->
                                    Log.e("ИЩУ:", "навигация к детализации рецепта id = $recipeId")
                                    navController.navigate("recipe_detail/$recipeId")
                                },
                                authViewModel = authViewModel
                            )
                        }

                        // Recipe detail screen (пример, если сделан)
                        composable(
                            route = "recipe_detail/{recipeId}",
                            arguments = listOf(
                                androidx.navigation.navArgument("recipeId") { type = androidx.navigation.NavType.LongType }
                            )
                        ) { backStackEntry ->
//                            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: 0L
                            val recipeId = backStackEntry.arguments?.getLong("recipeId") ?: error("recipeId не передан")
                            Log.e("ИЩУ:", "MainActivity: RecipeDetail открыт, id = $recipeId")

                            val detailViewModel: RecipeDetailViewModel = viewModel(
                                factory = RecipeDetailViewModelFactory(
                                    api = recipeApi,
                                    recipeId = recipeId
                                )
                            )

                            RecipeDetailScreen(
                                recipeId = recipeId,
                                recipesViewModel,
                                viewModelDetailRecipe = detailViewModel,
                                authViewModel = authViewModel,
                                navController = navController,
                                onBack = { navController.popBackStack() },
                            )

                        // Здесь вызываем RecipeDetailScreen
                            // RecipeDetailScreen(recipeId = recipeId, ...)
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



