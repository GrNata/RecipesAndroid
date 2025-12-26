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
import androidx.compose.runtime.derivedStateOf
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
    val scrollState = rememberLazyListState()

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

                error != null || recipe == null -> {
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

                recipe != null -> {
//                    Анимация шагов
                    val visibleStepsCount = remember { mutableStateOf(0) }
                    //    делаем Hero Image Animation через AnimatedVisibility
                    val imageVisible = remember { mutableStateOf(false) }

                    LaunchedEffect(recipe.steps) {
                        visibleStepsCount.value = 0
                        recipe.steps.forEachIndexed { index, _ ->
                            delay(250) // скорость появления
                            visibleStepsCount.value = index + 1
                        }
                        delay(80)
                        imageVisible.value = true
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
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Int.toDpComposable() : Dp {
    return with(LocalDensity.current) {
        this@toDpComposable.toDp()
    }
}