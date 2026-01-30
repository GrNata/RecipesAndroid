package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.core.graphics.component1
import com.grig.recipesandroid.data.model.ui.IngredientUi
import kotlin.math.max


@Composable
fun IngredientRow(
    ingredient: IngredientUi,
    onClick: () -> Unit,
    viewModel: RecipeDetailViewModel
) {
    val baseId by viewModel.baseIngredientId.collectAsState()

//    Анимация изменения числа
    val animatedAmount by animateFloatAsState(
        targetValue = ingredient.amount?.toFloat() ?: 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "ingredientAmount"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().height(40.dp)
    ) {
        Column() {
            Row() {
                IconButton(
                    modifier = Modifier.size(16.dp),
                    onClick = {
                        ingredient.amount?.let {
//                чтоб не уйти в ноль
                            val newValue = max(it * 0.9, 0.1)

                            viewModel.recalculateFrom(ingredient.id, newValue)
                        }
                    }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "-",
                        tint = Color(0xFF628AB4)
                    )
                }

                IconButton(
                    modifier = Modifier.height(16.dp).size(16.dp),
                    onClick = {
                        ingredient.amount?.let {
                            viewModel.recalculateFrom(ingredient.id, it * 1.1)
                        }
                    }) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = "+",
                        tint = Color(0xFF628AB4)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = ingredient.name,
                    modifier = Modifier
                        .weight(2f)
                        .clickable { onClick() },
                    color = if (ingredient.id == baseId)
                        Color(0xFF447DD4)
//                Color(0xFF447BB4)
                    else
                        Color(0xFF628AB4),
//                            Color(0xFF123D69),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = ingredient.amount?.let {
                        formatAmount(animatedAmount.toDouble(), ingredient.unit)
                    } ?: "",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
//            textAlign = TextAlign.End,
                    color = Color(0xFF628AB4),
                    style = MaterialTheme.typography.bodyMedium
                )
            }   //  Row

            // Черта под Row
            Divider(
                color = Color(0xFF628AB4),           // цвет линии
                thickness = 0.3.dp             // толщина линии
            )   //  Column
        }
    }   //  Row
}

//    Умное округление (г → кг)
    fun formatAmount(amoumt: Double, unit: String?) : String {
        return when (unit) {
            "г" ->  if (amoumt >= 1000) "${"%.2f".format(amoumt / 1000)} кг"
            else "${amoumt.toInt()} г"
            "мл" ->  if (amoumt >= 1000) "${"%.2f".format(amoumt / 1000)} л"
            else "${amoumt.toInt()} мл"

            "шт" ->  amoumt.toInt().toString() + " шт"
            else -> "%.1f".format(amoumt) + " шт"
        }
    }