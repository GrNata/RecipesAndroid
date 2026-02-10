package com.grig.recipesandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.gson.GsonBuilder
import com.grig.recipesandroid.data.api.AuthApi
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.FavoritesDataStore
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.network.AuthInterceptor
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.ui.admin.main.AdminViewModel
import com.grig.recipesandroid.ui.admin.main.AdminViewModelFactory
import com.grig.recipesandroid.ui.admin.statistic.AdminStatsViewModel
import com.grig.recipesandroid.ui.admin.statistic.AdminStatsViewModelFabrica
import com.grig.recipesandroid.ui.admin.auditLogs.AdminAuditViewModel
import com.grig.recipesandroid.ui.admin.auditLogs.AdminAuditViewModelFactory
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.colorScheme.MyAppTheme
import com.grig.recipesandroid.ui.my_recipes.AddEditRecipeViewModel
import com.grig.recipesandroid.ui.my_recipes.AddEditRecipeViewModelFactory
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModel
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModelFactory
import com.grig.recipesandroid.ui.navigation.AppNavGraph
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModelFactory
import com.grig.recipesandroid.ui.search_by_ingredients.SearchByIngredientViewModelFactory
import com.grig.recipesandroid.ui.search_by_ingredients.SearchByIngredientsViewModel
import com.grig.recipesandroid.utils.LocalDateTimeAdapter
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private lateinit var recipeApi: RecipeApi
    private lateinit var authApi: AuthApi
    private lateinit var tokenRepository: TokenRepository
    private lateinit var authRepository: AuthRepository

    private lateinit var ingredientRepository: IngredientRepository

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

//        // 2. AuthApi и RecipeApi через Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:9090/") // эмулятор Android
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

//        authApi = retrofit.create(AuthApi::class.java)
//        val recipeApiTemp = retrofit.create(RecipeApi::class.java)

        // 3. AuthRepository
//        authRepository = AuthRepository(authApi, tokenRepository)

        val authRetrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authApi = authRetrofit.create(AuthApi::class.java)



        authRepository = AuthRepository(authApi, tokenRepository)

        // 4. OkHttpClient с AuthInterceptor
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor(tokenRepository, authRepository))
//            .build() // 2️⃣ OkHttpClient с AuthInterceptor
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(
                    tokenRepository = tokenRepository,
                    authRepository = authRepository
                )
            )
            .build()


//        // 5. RecipeApi с клиентом, который знает про токены
//        recipeApi = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:9090/")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(RecipeApi::class.java)
//
        val favoritesLocalDataSource = FavoritesDataStore(applicationContext)
//
//        // 6. Repositories
//        val recipeRepository = RecipeRepository(recipeApi)
//        val favoritesRepository = FavoritesRepository(recipeApi, tokenRepository, local = favoritesLocalDataSource)

//        для LocalDateTime
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        // 3️⃣ ЕДИНСТВЕННЫЙ Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/")
            .client(okHttpClient)                     // 🔥 ВАЖНО
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // 4️⃣ API из одного Retrofit
        authApi = retrofit.create(AuthApi::class.java)
        recipeApi = retrofit.create(RecipeApi::class.java)

        // 5️⃣ Репозитории
        authRepository = AuthRepository(authApi, tokenRepository)
        val recipeRepository = RecipeRepository(recipeApi)
        val favoritesRepository = FavoritesRepository(recipeApi, tokenRepository, local = favoritesLocalDataSource)
        val categoryRepository = CategoryRepository(recipeApi)
        val ingredientRepository = IngredientRepository(recipeApi)
        val unitRepository = UnitRepository(recipeApi)

        // 7. Compose и NavHost
        setContent {
//            Моя цветовая схема
            MyAppTheme {
//                RecipesAndroidTheme {
                    val navController = rememberNavController()

                    // Создаём ViewModels
                    val authViewModel: AuthViewModel =
                        viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return AuthViewModel(
                                    authRepository,
                                    tokenRepository
                                ) as T
                            }
                        })

                    val recipesViewModel: RecipesViewModel = viewModel(
                        factory = RecipesViewModelFactory(
                            repository = recipeRepository,
                            favoritesRepository = favoritesRepository,
                            categoryRepository = categoryRepository,
                            ingredientRepository = ingredientRepository,
                            userIdFlow = authViewModel.userId
                        )
                    )

                    val myRecipesViewModel: MyRecipesViewModel = viewModel(
//                    parentEntry,
                        factory = MyRecipesViewModelFactory(recipeRepository, authViewModel)
                    )

                    val searchByIngredientsViewModel: SearchByIngredientsViewModel = viewModel(
                        factory = SearchByIngredientViewModelFactory(
                            recipeRepository,
                            ingredientRepository,
                            recipesViewModel
                        )
                    )

//                val recipeDetailViewModel: RecipeDetailViewModel = viewModel(
//                    factory = RecipeDetailViewModelFactory(recipeApi)
//                )

                    val addEditRecipeViewModel: AddEditRecipeViewModel = viewModel(
                        factory = AddEditRecipeViewModelFactory(
                            recipeRepository = recipeRepository,
                            categoryRepository = categoryRepository,
                            ingredientRepository = ingredientRepository,
                            unitRepository = unitRepository,
                            authViewModel = authViewModel,
                            recipesViewModel,
                            myRecipesViewModel,
                            navController
                        )
                    )

                    val adminViewModel: AdminViewModel = viewModel(
                        factory = AdminViewModelFactory(
                            authRepository,
                            ingredientRepository,
                            categoryRepository,
                            recipesViewModel,
                            navController
                        )
                    )

                val adminAuditViewModel: AdminAuditViewModel = viewModel(
                    factory = AdminAuditViewModelFactory(
                        authRepository
                    )
                )

                val adminStatsViewModel: AdminStatsViewModel = viewModel(
                    factory = AdminStatsViewModelFabrica(
                        authRepository
                    )
                )

                    AppNavGraph(
                        navController = navController,
                        api = recipeApi,
                        authViewModel = authViewModel,
                        recipeRepository = recipeRepository,
                        categoryRepository = categoryRepository,
                        tokenRepository = tokenRepository,
                        ingredientRepository = ingredientRepository,
                        unitRepository = unitRepository,
                        applicationContext = applicationContext,
                        recipeViewModel = recipesViewModel,
                        addEditRecipeViewModel = addEditRecipeViewModel,
                        myRecipesViewModel = myRecipesViewModel,
                        searchByIngredientsViewModel = searchByIngredientsViewModel,
                        adminViewModel = adminViewModel,
                        adminAuditViewModel = adminAuditViewModel,
                        adminStatsViewModel
//                    recipeDetailViewModel = recipeDetailViewModel
                    )
//                }   //  RecipeAndroidTheme
            }
        }   //  setContent
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



