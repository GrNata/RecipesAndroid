package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import coil.compose.AsyncImage
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import kotlinx.coroutines.delay
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
//private fun RecipeDetailLoaded(
fun RecipeDetailLoaded(
    recipeViewModel: RecipesViewModel,
    recipe: Recipe,
    onBack: () -> Unit,
    recipeId: Long,
    snackbarHostState: SnackbarHostState
) {
    val favoritesSet by recipeViewModel.favorites.collectAsState()
    val isFavorite = recipeId in favoritesSet

    val scope = rememberCoroutineScope()   //  <-- добавляем корутинный скоуп

    val visibleStepsCount = remember { mutableStateOf(0) }
    val imageVisible = remember { mutableStateOf(false) }

    LaunchedEffect(recipe.id) {   //  ключ СТАБИЛЬНЫЙ
        visibleStepsCount.value = 0
        recipe.steps.forEachIndexed { index, _  ->
            delay(250)
            visibleStepsCount.value = index + 1
        }
        imageVisible.value = true
    }

    // LazyColumn + UI

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().height(20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                val scale by animateFloatAsState(targetValue = if (isFavorite) 1.3f else 1f)

                IconButton(
                    onClick = {
                        recipeViewModel.toggleFavorite(recipe.id)
                        // показываем SnackBar
                        val message = if (isFavorite) "Рецепт удален из избранного" else "Рецепт добавлен в избранное"
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                    modifier = Modifier.scale(scale)
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = "Избраное",
                        tint = Color.Red,
//                        tint = if (isFavorite) Color.Red else Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
        // --- ОПИСАНИЕ ---
        item {
            Text(
                text = recipe.description ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF9A3B3B),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // --- СТРОКА: КАРТИНКА + ИНФО ---
        // --- ROW: картинка слева + категория и ингредиенты справа ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                // Картинка слева (сжимаемая)
                recipe.image?.let {
                    val scrollState = rememberLazyListState()
                    val imageHeight by animateDpAsState(
                        targetValue = max(8.dp, 120.dp - scrollState.firstVisibleItemScrollOffset.dp)
//                        targetValue = (120.dp - scrollState.firstVisibleItemScrollOffset.dp)
//                            .coerceAtLeast(8.dp),
//                        animationSpec = tween(300)
                    )
//                                    Fake shared image (scale animation)
                    AnimatedVisibility(
                        visible = imageVisible.value,
//                                        enter = fadeIn() + slideInVertically { it / 2 },
                        enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.85f, animationSpec = tween(1000)),
                        exit = fadeOut()
                    ) {
                        AsyncImage(
                            model = it,
                            contentDescription = recipe.name,
                            modifier = Modifier
//                                                .height(120.dp)
//                                                .fillMaxWidth()
                                .height(imageHeight)
                                .clip(RoundedCornerShape(20.dp))
                        )

                    }

                    Spacer(Modifier.width(12.dp))
                }

                // Правая колонка — ВСЕГДА
                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp)
                ) {
                    Text(
                        text = "Категория: ${
                            recipe.categories.joinToString { it.name.lowercase() }
                        }",
                        color = Color(0xFF7E889F),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Ингредиенты:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF656A77)
                    )

                    recipe.ingredients.forEach {
                        Text(
                            text = "• ${it.ingredient.name}: ${it.amount ?: ""} ${it.unit}",
                            color = Color(0xFF656A77),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }   // Row
        }

        // --- ШАГИ ---
        item {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Шаги приготовления:",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF123C69),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(recipe.steps.size) { index ->
            AnimatedVisibility(
                visible = index < visibleStepsCount.value,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { it / 2 }
                )
            ) {
                Text(
                    text = "${index + 1}. ${recipe.steps[index]}",
                    color = Color(0xFF123C69),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }  //  LazyColumn
    }

}