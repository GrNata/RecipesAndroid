package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.grig.recipesandroid.domain.model.Recipe

//@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalFoundationApi::class)
@Composable
fun RecipeItem(
    recipe: Recipe,
    query: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFEEE2DC))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
//                .background(Color(0xFFF6E5D7))
        ) {
            Row {
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
                Column {
//                    Text(
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
                recipe.ingredients.forEach { ing ->
                    Text(
//                        text = "${ing.ingredient.name}: ${ing.amount} ${ing.unit ?: ""}".trim(),
                        text = "${ing.ingredient.name}, ".trim().lowercase(),
                        color = Color(0xFF123C69))
                }
            }
//            Spacer(modifier = Modifier.padding(4.dp))
//            Text(
//                text = "Шаги:",
//                color = Color(0xFF9A663B)
//                )
//            recipe.steps.forEach { step ->
//                Text(
//                    text = "-$step",
//                    color = Color(0xFF9A663B)
//                    )
//            }
        }
    }
}
