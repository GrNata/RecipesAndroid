package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.toUi

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
    onDeleteClick: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() }            // переход к detail
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFEEE2DC))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
//                .background(Color(0xFFF6E5D7))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(15.dp).padding(0.dp),
                horizontalArrangement = Arrangement.End
            ) {
            // Только для моих рецептов - кнопки добавить и удалить
                if (isOwner) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = {
//                            onEditClick?.invoke()
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Редактировать")
                        }
                        IconButton(onClick = {
//                            onDeleteClick?.invoke()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить")
                        }
                    }
                }

                val scale by animateFloatAsState(targetValue = if (isFavorite) 1.3f else 1f)

                IconButton(
                    onClick = { viewModel.toggleFavorite(recipe.id) },
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
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                // Колонка с текстом и Spacer между текстом и иконкой
//                Row(modifier = Modifier.fillMaxWidth()) {
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

            Spacer(modifier = Modifier.padding(4.dp))
//            Column {
            Row {
                val ingredientsUi = recipe.ingredients.map { it.toUi() }
//                recipe.ingredients.forEach { ing ->
                ingredientsUi.forEach { ing ->
                    Text(
//                        text = "${ing.ingredient.name}: ${ing.amount} ${ing.unit ?: ""}".trim(),
                        text = "${ing.ingredient.name}, ".trim().lowercase(),
                        color = Color(0xFF123C69)
                    )
//                    Text(text = ing.unit?.label ?: "")
                }
            }
        }
    }
}
