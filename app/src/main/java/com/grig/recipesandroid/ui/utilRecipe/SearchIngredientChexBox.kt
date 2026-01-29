package com.grig.recipesandroid.ui.utilRecipe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.ui.search_by_ingredients.SearchByIngredientsViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchIngredientChexBox(
    ingredients: List<IngredientDto>,
    ingredientsViewModel: SearchByIngredientsViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val selectedIngredientIds = ingredientsViewModel.selectedIngredientIds
    val searchRecipes by ingredientsViewModel.searchRecipes


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF7EDE9))
    ) {

        items(ingredients) { ingredient ->
            val isChecked = selectedIngredientIds.contains(ingredient.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        if (isChecked) {
                            selectedIngredientIds.remove(ingredient.id)
                        } else {
                            if (selectedIngredientIds.size < 10) {
//                                if (selectedIngredientIds.size < 3) {
                                selectedIngredientIds.add(ingredient.id)
                            } else {
                                scope.launch {   // <-- запускаем корутину
                                    snackbarHostState.showSnackbar(
                                        "Можно выбрать не более 10 ингредиентов"
//                                            "Можно выбрать не более 3 ингредиентов"
                                    )
                                }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        if (checked) {
                            if (selectedIngredientIds.size < 10) {
//                                if (selectedIngredientIds.size < 3) {
                                selectedIngredientIds.add(ingredient.id)
                            }
                        } else {
                            selectedIngredientIds.remove(ingredient.id)
                        }
                    }
                )
                Text(
                    text = ingredient.name,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF123C69)
                )
            }
        }
//                // Список найденных рецептов
//                searchRecipes?.forEach { recipe ->
//                    item {
//                        RecipeItem(
//                            viewModel = recipesViewModel,
//                            recipe = recipe,
//                            query = "",
//                            isFavorite = false,
//                            isOwner = false,
//                            onFavoriteClick = {},
//                            onClick = { navController.navigate("recipe_detail/${recipe.id}") },
//                            onEditClick = {},
//                            onDeleteClick = {}
//                        )
//                    }
//                }
    }   //  LazyColumn
}