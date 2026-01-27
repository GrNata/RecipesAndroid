package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.ui.IngredientUi

@Composable
fun NumberOfServings(
    viewModel: RecipeDetailViewModel
) {

    val serving by viewModel.currentServings.collectAsState()

    Row() {

        IconButton(
            modifier = Modifier.size(16.dp),
            onClick = {
                viewModel.recalculateForServings(serving - 1)
            }
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "-",
                tint = Color(0xFFA14111)
            )
        }

        IconButton(
            modifier = Modifier.height(16.dp).size(16.dp),
            onClick = {
                viewModel.recalculateForServings(serving + 1)
            }
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "+",
                tint = Color(0xFFA14111)
            )
        }

        Text(
            text = "Кол-во порций:  ${serving}",
            color = Color(0xFF692705),
//            color = Color(0xFF6C687B),
            style = MaterialTheme.typography.bodyLarge
        )
    }


}