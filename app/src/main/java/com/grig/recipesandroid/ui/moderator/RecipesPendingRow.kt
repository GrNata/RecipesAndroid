package com.grig.recipesandroid.ui.moderator

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailScreen
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.millisToDateString

@Composable
fun RecipesPendingRow(
    recipe: RecipeDto,
    recipesViewModel: RecipesViewModel,
//    detailViewModel: RecipeDetailViewModel,
//    authViewModel: AuthViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Log.d("MODERATOR:", "RecipesPendingRow: before RecipeDetailContent recipe =${recipe}")
        Row(
            modifier = Modifier
                .clickable {
//                    переход в детализацию
                    navController.navigate("recipe_detail/${recipe.id}")
                }
        ) {
            Column(
                Modifier.weight(2f)
            ) {
                Text("")
                Text(
                    text = recipe.name,
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                Modifier.weight(2f)
            ) {
                Text("")
                Text(
                    text = recipe.createdAt,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = {
                        recipesViewModel.approveRecipe(requireNotNull(recipe.id))
                    },
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Одобрить рецепт",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(
                Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = {
                        recipesViewModel.rejectRecipe(requireNotNull(recipe.id))
                    },
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Отклонить рецепт",
                        tint = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}