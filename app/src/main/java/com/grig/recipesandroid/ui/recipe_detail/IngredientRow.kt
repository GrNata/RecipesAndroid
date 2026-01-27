package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.ui.IngredientUi

@Composable
fun IngredientRow(
    ingredient: IngredientUi,
    onClick: () -> Unit
) {
    Text(
//        text = "• ${ingredient.name}: ${ingredient.amount?.let { "$.1f".format(it) } ?: ""} ${ingredient.unit ?: ""}",
        text = "• ${ingredient.name}: ${ingredient.amount?.let { 
            if (it % 1.0 == 0.0) it.toInt().toString()
            else "%.1f".format(it) 
        } ?: ""} ${ingredient.unit ?: ""}",
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        color = Color(0xFF656A77),
        style = MaterialTheme.typography.bodyMedium
    )
}