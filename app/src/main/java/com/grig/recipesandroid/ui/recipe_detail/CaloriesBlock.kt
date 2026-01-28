package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.domain.model.Recipe

@Composable
fun CaloriesBlock(
    recipe: Recipe
) {

    val servings = recipe.baseServings ?: 1
    val calories = recipe.totalCalories?.let {  it }
    Row() {
        Text(
            text = "Калории:",
            color = Color(0xFF6C687B),
            style = MaterialTheme.typography.bodyLarge
        )

//                        recipe.totalCalories?.let {  calories ->
        if (calories != null) {
            Text(
                text = "${calories} кКал",
                color = Color(0xFF6C687B),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 6.dp)
            )
        } else {
            Text(
                text = "Калории не указаны",
                color = Color(0xFF6C687B),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
    if (calories != null) {
        Text(
            text = "(≈ ${calories?.let { it / servings }} кКал на 1 порцию)",
            color = Color(0xFF6C687B),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}