package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.grig.recipesandroid.data.mapper.toIngredientUi
import com.grig.recipesandroid.data.model.dto.RecipeStatus
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.launch


@Composable
//private fun RecipeDetailLoaded(
fun RecipeDetailLoaded(
    recipeViewModel: RecipesViewModel,
    recipeDetailViewModel: RecipeDetailViewModel,
    authViewModel: AuthViewModel,
    recipe: Recipe,
    isMyDetail: Boolean,
    onBack: () -> Unit,
    recipeId: Long,
    snackbarHostState: SnackbarHostState,
//    isModerator: Boolean? = false
) {
    val isModerator by authViewModel.isModerator.collectAsState()
    val isAdmin by authViewModel.isAdmin.collectAsState()
    val isAdminModeratorDetail by recipeDetailViewModel.isAdminModeratorDetail.collectAsState()
    val isModeratorDetail by recipeViewModel.isModeratorDetail.collectAsState()


    val favoritesSet by recipeViewModel.favorites.collectAsState()
    val isFavorite = recipeId in favoritesSet

    val scope = rememberCoroutineScope()   //  <-- добавляем корутинный скоуп

    val visibleStepsCount = remember { mutableStateOf(0) }
    val imageVisible = remember { mutableStateOf(false) }

    //    Для блокировки кнопки во время запроса - MODERATOR
    var loading by remember { mutableStateOf(false) }

    // с сервера статус синхронизируется, и при скролле не будет багов с переиспользованием ячеек.
    var currentStatus by remember(recipe.id, recipe.status) {
        mutableStateOf(recipe.status)
    }

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
                if ((isAdmin || isModerator) && recipe.status.name == "PENDING" && isModeratorDetail == true) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(30.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Button(
                                onClick = { recipeViewModel.rejectRecipe(recipeId) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD38484),
                                    contentColor = Color(0xFF473972)
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                ),
                                border = BorderStroke(1.dp, Color(0xFF6C1B1B)),
                                contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                                modifier = Modifier.size(110.dp)
                            ) {
                                Text("Отклонить")
                            }
                        }

                        Column(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
                                onClick = { recipeViewModel.approveRecipe(recipeId) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF79C279),
                                    contentColor = Color(0xFF473972)
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                ),
                                border = BorderStroke(1.dp, Color(0xFF165616)),
                                contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                                modifier = Modifier.size(110.dp)
                            ) {
                                Text("Опубликовать")
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))

                }
                else {
//                ++++++++++
            val scale by animateFloatAsState(targetValue = if (isFavorite) 1.3f else 1f)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(30.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            Modifier.weight(4f),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = {
                                    recipeViewModel.toggleFavorite(recipe.id)
                                    // показываем SnackBar
                                    val message =
                                        if (isFavorite) "Рецепт удален из избранного" else "Рецепт добавлен в избранное"
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
                        Log.d("MODERATOR", "RecipeDetailLoaded: isMyDetail = $isMyDetail")
                        if (isMyDetail) {
                            Column(
                                Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                                ) {
                                IconButton(
                                    onClick = {
                                        //    Для блокировки кнопки во время запроса - MODERATOR
                                        // Проверяем текущий ЛОКАЛЬНЫЙ статус
                                        if (!loading && currentStatus == RecipeStatus.DRAFT) {
                                            loading = true
                                            // МГНОВЕННО меняем статус на экране (звездочка тут же станет желтой)
                                            val oldStatus = currentStatus
                                            currentStatus = RecipeStatus.PENDING    //  optimistic UI

                                            scope.launch {
                                                // Отправляем запрос на сервер в фоне
                                                val success = recipeViewModel.sendToModeration(recipe.id)

                                                if (!success) {
                                                    currentStatus = oldStatus   //  откат
                                                }
                                                loading = false
                                            }
                                        }
////                        отправить на модерацию

//                                        //                            Из заблокированного в черновик - пользователь
//                                        Log.d("MODERATOR", "RecipeItem: before if recipe.name: ${recipe.name}, RecipeStatus.REJECTED = ${recipe.status}")
//                                        if (recipe.status == RecipeStatus.REJECTED || recipe.status == RecipeStatus.DRAFT) {
//                                            Log.d("MODERATOR", "RecipeItem: if recipe.name: ${recipe.name}, RecipeStatus.REJECTED = ${recipe.status}")
//
//                                            val oldStatus = currentStatus
//                                            if (recipe.status == RecipeStatus.REJECTED) {
//                                                currentStatus =
//                                                    RecipeStatus.DRAFT    //  optimistic UI
//                                            } else {
//                                                recipe.status == RecipeStatus.PENDING
//                                            }
//
//                                            scope.launch {
//                                                // Отправляем запрос на сервер в фоне
//                                                val success = recipeViewModel.updateStatusFromRejectedToDraft(recipe.id, currentStatus)
//
//                                                if (!success) {
//                                                    currentStatus = oldStatus   //  откат
//                                                }
//                                                loading = false
//                                            }
//                                        }
                                    },
                                ) {
                                    Log.d("MODERATOR", "RecipeItem: if recipe.name: ${recipe.name}, currentStatus = ${currentStatus}")
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Избраное",
                                        tint =
                                            when (currentStatus) {
                                                RecipeStatus.DRAFT -> Color(0xFF848484)
                                                RecipeStatus.PENDING -> Color(0xFFFFD200)
                                                RecipeStatus.APPROVED -> Color(0xFF3DA028)
                                                RecipeStatus.REJECTED -> Color(0xFFBF3030)
                                            },

                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }

//                ++++++++++++++
                    }
            }   //  else

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
                        targetValue = max(
                            8.dp,
                            120.dp - scrollState.firstVisibleItemScrollOffset.dp
                        )
                    )
                    AnimatedVisibility(
                        visible = imageVisible.value,
                        enter = fadeIn(animationSpec = tween(1000)) + scaleIn(
                            initialScale = 0.85f,
                            animationSpec = tween(1000)
                        ),
                        exit = fadeOut()
                    ) {
                        AsyncImage(
                            model = it,
                            contentDescription = recipe.name,
                            modifier = Modifier
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
                        text = "Категория:",
                        color = Color(0xFF6C687B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = recipe.categories.joinToString { it.categoryValue.lowercase() },
                        color = Color(0xFF7E889F),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

//                    Калории
                    CaloriesBlock(recipe)

                    Spacer(modifier = Modifier.height(24.dp))

//                    Количество порций
                    NumberOfServings(recipeDetailViewModel)

                }   //  Column
            }   // Row
        }   //  item

//            +++++++++++++++++
            item {
//            Ингредиенты
                Spacer(Modifier.height(16.dp))
                val ingredientsUi = recipe.ingredients.map { it.toIngredientUi() }
                RecipeIngredientsBlock(recipeDetailViewModel)

                Spacer(modifier = Modifier.height(20.dp))
            }

//        }
//        ++++++++++++++++++++++

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