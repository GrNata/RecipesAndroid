package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.recipesandroid.data.model.dto.RecipeStatus
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.toUi
import com.grig.recipesandroid.ui.utilRecipe.HighlightedText
import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalFoundationApi::class)
@Composable
fun RecipeItem(
    viewModel: RecipesViewModel,
    recipe: Recipe,
    query: String,
    isFavorite: Boolean,
    isOwner: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    onEditClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
) {
    //  Создаем локальное состояние статуса. - MODERATOR - изменения цвета кнопки
    // remember(recipe.id, recipe.status) гарантирует, что при реальном изменении данных
    // с сервера статус синхронизируется, и при скролле не будет багов с переиспользованием ячеек.
    var currentStatus by remember(recipe.id, recipe.status) {
        mutableStateOf(recipe.status)
    }
//    Для блокировки кнопки во время запроса - MODERATOR
    var loading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() }            // переход к detail
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFF8F7))
//        colors = CardDefaults.cardColors(Color(0xFFEEE2DC))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
//                .background(Color(0xFFF6E5D7))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(21.dp),
                horizontalArrangement = Arrangement.End
            ) {

            // Только для моих рецептов - кнопки добавить и удалить
                if (isOwner) {
                        IconButton(
                            onClick = {
                            onEditClick?.invoke()
                        },
                            modifier = Modifier.size(18.dp) // Размер кнопки
                            ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = MaterialTheme.colorScheme.surface
                            )
                        }

                        // Отступ 8 dp между кнопками
                        Spacer(modifier = Modifier.width(16.dp))

                        IconButton(
                            onClick = {
                            onDeleteClick?.invoke()
                        },
                            modifier = Modifier.size(18.dp)  // Размер кнопки
                            ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Удалить",
                                tint = MaterialTheme.colorScheme.surface
                                )
                        }

                    // Отступ 8 dp между кнопками
                    Spacer(modifier = Modifier.width(16.dp))

                }

//                val scale by animateFloatAsState(targetValue = if (isFavorite) 1.3f else 1f)
                val scale by animateFloatAsState(targetValue = if (isFavorite) 1f else 0.9f)

                IconButton(
                    onClick = { viewModel.toggleFavorite(recipe.id) },
//                    modifier = Modifier.scale(scale)
                    modifier = Modifier
                        .size(20.dp)
                        .scale(scale)  // Размер кнопки
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

//                MODERATOR
                Spacer((Modifier.width(16.dp)))

                val scope = rememberCoroutineScope()

                if (loading) {
                    Box() {
                        CircularProgressIndicator()
                    }
                }
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
                                val success = viewModel.sendToModeration(recipe.id)

                                if (!success) {
                                    currentStatus = oldStatus   //  откат
                                }
                                loading = false
                            }
                        }
////                        отправить на модерацию
//                        if (recipe.status == RecipeStatus.DRAFT) {
//                            viewModel.sendToModeration(recipe.id)
//                        }
                    },
                    modifier = Modifier.size(18.dp)  // Размер кнопки
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Модерация (черновик, на модерации, опубликован, отклонен)",
                        //  Отрисовываем цвет в зависимости от ЛОКАЛЬНОГО currentStatus, а не recipe.status
//                        tint = when (recipe.status) {
                        tint = when (currentStatus) {
                            RecipeStatus.DRAFT -> Color(0xFF848484)
                            RecipeStatus.PENDING -> Color(0xFFFFD200)
                            RecipeStatus.APPROVED -> Color(0xFF3DA028)
                            RecipeStatus.REJECTED -> Color(0xFFBF3030)
                        }
                    )
                }

//                Log.d("СЕРДЦЕ", "isFavorite = $isFavorite, recipeId = ${recipe.id}")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Изображение слева
                recipe.image?.let {
//                    Fake shared image (scale animation)
                    AsyncImage(
                        model = recipe.image,
                        contentDescription = recipe.name,
                        modifier = Modifier
//                            .height(80.dp)
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Колонка с текстом и Spacer между текстом и иконкой
//                Row(modifier = Modifier.fillMaxWidth()) {
                Log.d("ADD RECIPE-newEdit", "RecipeItem: name: ${recipe.name}, description=${recipe.description}")
                    Column {
                        HighlightedText(
                            text = recipe.name,
                            query = query,
//                        style = MaterialTheme.typography.titleMedium,
                            style = MaterialTheme.typography.titleLarge,
//                        color = Color(0xFFAC3B61)
                            color = Color(0xFF9A3B3B)
                        )
                        recipe.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
//                            color = Color(0xFFBAB2B5)
                                color = Color(0xFFB2A193),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
            }     // Row

            Spacer(modifier = Modifier.padding(1.dp))
//            Column {
            Row {
                val ingredientsUi = recipe.ingredients.map { it.toUi() }
//                recipe.ingredients.forEach { ing ->
//                Log.d("INGREDIENT-UI", "RecipeItem: IngredientUi: ${ingredientsUi}")


                ingredientsUi.forEach { ing ->
                    Text(
//                        text = "${ing.ingredient.name}: ${ing.amount} ${ing.unit ?: ""}".trim(),
                        text = "${ing.ingredient.name}, ".trim().lowercase(),
                        color = Color(0xFF123C69)
                    )
//                    Text(text = ing.unit?.label ?: "")
                }
            }

            Spacer(Modifier.height(10.dp))
//            Автор и дата создания
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        text = recipe.createdAt,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = recipe.author.username,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
