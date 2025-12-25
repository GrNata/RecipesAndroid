package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

//отдельный RecipeDetailContent
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailContent(
    recipe: Recipe?,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit
) {

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
                    Text(
                        text = error,
                        color = Color(0xFF9A3B3B),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                recipe != null -> {
//                    Анимация шагов
                    val visibleStepsCount = remember { mutableStateOf(0) }

                    LaunchedEffect(recipe.steps) {
                        visibleStepsCount.value = 0
                        recipe.steps.forEachIndexed { index, _ ->
                            delay(250) // скорость появления
                            visibleStepsCount.value = index + 1
                        }
                    }


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

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
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {

                                // Картинка слева (если есть)
                                recipe.image?.let {
                                    AsyncImage(
                                        model = it,
                                        contentDescription = recipe.name,
                                        modifier = Modifier
                                            .width(120.dp)
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )

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
                            }
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

//                        items(recipe.steps.size) { index ->
//                            Text(
//                                text = "${index + 1}. ${recipe.steps[index]}",
//                                color = Color(0xFF123C69),
//                                style = MaterialTheme.typography.bodyMedium,
//                                modifier = Modifier.padding(bottom = 4.dp)
//                            )
//                        }

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
                        }
                    }
                }
            }
        }
    }
}