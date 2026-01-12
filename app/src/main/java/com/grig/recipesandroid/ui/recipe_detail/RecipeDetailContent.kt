package com.grig.recipesandroid.ui.recipe_detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.recipesandroid.domain.model.Recipe
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//отдельный RecipeDetailContent
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailContent(
    recipe: Recipe?,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit
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
            CenterAlignedTopAppBar(
//            TopAppBar(
                title = {
                    Text(
                        text = recipe?.name ?: "Рецепт",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF245C5C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (recipe != null) {
                        IconButton(onClick = {
                            // Share recipe через Intent
                            val ingredientsText = recipe.ingredients.joinToString("\n") {ri ->
                                "- ${ri.ingredient.name}: ${ri.amount ?: ""} ${ri.unit}"
                            }

                            val steps = recipe.steps.joinToString("\n") {step ->
                                "- ${step}"
                            }
                            val shareText = """
                                Рецепт: ${recipe.name}
                                Ингрериенты:
                                ${ingredientsText}
                                Шаги приготовления:
                                ${steps}
                            """.trimIndent()

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Поделиться рецептом"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Поделиться рецептом")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEEE2DC)
                )
            )
        }
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
                    RecipeDetailLoaded(recipe, onBack)
                }
            }   // when
        }
    }
}

//@Composable
//fun Int.toDpComposable() : Dp {
//    return with(LocalDensity.current) {
//        this@toDpComposable.toDp()
//    }
//}