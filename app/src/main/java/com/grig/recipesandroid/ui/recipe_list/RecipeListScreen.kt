package com.grig.recipesandroid.ui.recipe_list


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.grig.recipesandroid.ui.app_top_bar.AppTopBar
import com.grig.recipesandroid.ui.auth.AuthViewModel

//   разделяем UI и state - RecipeListContent
// RecipeListScreen получает ViewModel и передаёт данные в RecipeListContent.
@Composable
fun RecipeListScreen(
    viewModel: RecipesViewModel,
    navController: NavController,
    onRecipeClick: (Long) -> Unit,
    authViewModel: AuthViewModel
) {
    // 🔹 флаг для отображения LoginScreen
    var showLoginScreen by remember { mutableStateOf(false) }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val accessToken by authViewModel.accessToken.collectAsState(initial = null)

    Log.d("СЕРДЦЕ - TOKEN RecipeListScreen", "accessToken = ${accessToken?.take(10)}")

    val favoritesSet by viewModel.favorites.collectAsState()

    val recipes = viewModel.recipesPagingFlow.collectAsLazyPagingItems()
    val query by viewModel.query.collectAsState()

//    фильтрация избранных
    var showOnlyFavorites by remember { mutableStateOf(false) }

    val userId by authViewModel.userId.collectAsState()

//  В Scaffold передаём snackbarHost = { SnackbarHost(hostState = snackbarHostState) }.
//	•	LaunchedEffect(message) слушает messageFlow из ViewModel и показывает Snackbar.
//	•	Кнопка Retry на Snackbar вызывает viewModel.retryRecipes()

    val snackbarHostState = remember { SnackbarHostState() }
    val message by viewModel.messageFlow.collectAsState("")
    
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val isModerator by authViewModel.isModerator.collectAsState()

    viewModel.checkIsModeratorDetail(false)


    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            // Показываем Snackbar
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retryRecipes()        //      вызываем retry при клике на Retry
            }
        }
    }


    Scaffold(
        containerColor = Color(0xFFF7F3EC),

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
//            Log.d("ADMIN", "RecipeListScreen isAuthenticated = $isAuthenticated")
            Log.d("ADMIN", "RecipeListScreen: isModerator = $isModerator")

            val authRestored by authViewModel.authStateRestored.collectAsState()

            AppTopBar(
                title = "Рецепты",
                isAuthenticated = isAuthenticated,
                isAdmin = isAdmin,
                isModerator = isModerator,
                showMyRecipes = isAuthenticated && authRestored,
                onLoginClick = { navController.navigate("login") },
                onLogoutClick = {
                    authViewModel.logout()
                },
                onMyRecipesClick = {
                    Log.d("CATEGORY-ch", "RecipeListScreen: Navigating to MyRecipesScreen")
                    navController.navigate("my_recipes") {
                        launchSingleTop = true
                    }
                },
                onSearchByIngredients = { navController.navigate("search_ingredients") },
                onAdmin = { navController.navigate("admin") },
                onModerator = { navController.navigate("moderator") }
            )
        }
    ) { paddingValues ->

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .background(Color(0xFFF7F3EC))
                .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
//                        .background(Color(0xFFCBCAD2)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //        поиск / фильтрация
                    OutlinedTextField(
                        value = query,
                        onValueChange = { newText ->
                            viewModel.setQuery(newText)
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .weight(1f),
                        placeholder = {
                            Text(
                                "Поиск рецептов…",
//                                color = MaterialTheme.colorScheme.onTertiary,
                                color = Color(0xFF663D4B),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.onTertiary,        // рамка при фокусе
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,     // рамка без фокуса
//                            errorBorderColor = Color.Red,         // рамка в состоянии ошибки
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,      // цвет лейбла при фокусе
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,      // цвет лейбла без фокуса
                            focusedTextColor = Color(0xFF663D4B)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    //      кнопка фильтрации избранного
                    FilterChip(
                        selected = showOnlyFavorites,
                        onClick = { showOnlyFavorites = !showOnlyFavorites },
                        label = { Text("Любимые") },
                        colors = FilterChipDefaults.filterChipColors(
//                            containerColor = Color(0xFFE8DFE2),
                            containerColor = Color(0xFFF7F3EC),
                            labelColor = Color(0xFF663D4B),
                            selectedLabelColor = Color(0xFFE8DFE2),
                            selectedContainerColor = Color(0xFF883F58)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color =  MaterialTheme.colorScheme.primary
                        )
                    )
                }   //  Row

                Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFfffcfa))) {
                    RecipeListContent(
                        viewModel = viewModel,
                        isAuthenticated = isAuthenticated,
                        recipes = recipes,
                        query = query,
                        favorites = favoritesSet,       //  StateFlow из RecipesViewModel
                        onFavoriteClick = { recipeId -> viewModel.toggleFavorite(recipeId) },
                        showOnlyFavorites = showOnlyFavorites,
                        onToggleFavoritesFilter = {
                            showOnlyFavorites = !showOnlyFavorites
                        },
                        onRecipeClick = { id ->
                            Log.d(
                                "11-ИЩУ:",
                                "RecipeListScreen: id = ${id} navigate to recipe_detail/$id"
                            )
                            navController.navigate("recipe_detail/$id")
                        },
                        navController
                    )
                }
            }   //  Column
//        }           // else
    }

}






