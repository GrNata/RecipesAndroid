package com.grig.recipesandroid.ui.recipe_detail

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.domain.model.Recipe
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

//отдельный RecipeDetailContent
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailContent(
    recipe: Recipe?,
    loading: Boolean,
    error: String?,
    isAuthenticated: Boolean,
    onBack: () -> Unit,
    navController: NavController,
    authViewModel: AuthViewModel,
    recipeViewModel: RecipesViewModel,
    recipeDetailViewModel: RecipeDetailViewModel,
    recipeId: Long,
    snackbarHostState: SnackbarHostState
) {
//    val scrollState = rememberLazyListState()

//    val maxHeight = 120.dp
//    val minHeight = 20.dp
//
//    // Считаем текущую высоту картинки в зависимости от scroll
////    with(density) даёт доступ к функции .toDp()
//    val density = LocalDensity.current
//    val imageHeight by derivedStateOf {
//        val offsetDp = with(density) { scrollState.firstVisibleItemScrollOffset.toDp() }
//        (120.dp - offsetDp).coerceAtLeast(20.dp) // minHeight = 50.dp
//    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            val authRestored by authViewModel.authStateRestored.collectAsState()

            Log.d("ИЩУ:", "RecipeDetailContent: before AppTopBar recipeId=$recipeId")
            Log.d("ИЩУ:", "RecipeDetailContent: before AppTopBar recipe: $recipe")

            AppTopBar(
                title = recipe?.name ?: "Детали рецепта",
                isAuthenticated = isAuthenticated,
                showMyRecipes = authRestored && isAuthenticated,
                onBack = onBack,
//                onLoginClick = { navController.navigate("login") },
                onLoginClick = {
                    //    для -  «возврата на экран с которого повторное логирование»
                    authViewModel.requireLogin("recipe_detail/${recipeId}")
                    navController.navigate("login")
                               },
                onLogoutClick = { authViewModel.logout() },
                onShareClick = if (recipe != null && isAuthenticated) {
                    {
                        val ingredientsText = recipe.ingredients.joinToString("\n") { ri ->
                            "- ${ri.ingredient.name}: ${ri.amount ?: ""} ${ri.unit}"
                        }

                        val stepsText = recipe.steps.joinToString("\n") { step -> "- $step" }

                        val shareText = """
                        Рецепт: ${recipe.name}
                        Ингредиенты:
                        $ingredientsText
                        Шаги приготовления:
                        $stepsText
                    """.trimIndent()

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
                    }
                } else null,
                onMyRecipesClick = {
                    navController.navigate("my_recipes")
                },
                onSearchByIngredients = {}
//                authViewModel = authViewModel
            )
        }       //  topBar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFEEE2DC))
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                error != null -> {
//                     Empty / Error State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Иконка / Emoji
                        Text(
                            text = "\uD83D\uDE1E",      // печальный смайл
                            fontSize = 48.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = error ?: "Рецепт не найден",
                            color = Color(0xFF9A3B3B),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Пожалуйста, вернитесь назад или попробуйте другой рецепт",
                            color = Color(0xFF7E889F),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка «Назад»
                        Button(onClick = onBack) {
                            Text(text = "Назад")
                        }
                    }
                }
                recipe == null -> {
                    // ❗ ОБЯЗАТЕЛЬНО
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Рецепт загружается…",
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    Log.d("CICLE NAV_TRACE", "RecipeDetailContent before RecipeDetailLoaded")
                    RecipeDetailLoaded(
                        recipeViewModel,
                        recipeDetailViewModel,
                        recipe,
                        onBack,
                        recipeId,
                        snackbarHostState
                    )
                }
            }   // when
        }
    }
}
