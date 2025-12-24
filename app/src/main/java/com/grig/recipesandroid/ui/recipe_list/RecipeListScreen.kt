package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.grig.recipesandroid.data.model.CategoryDto
import com.grig.recipesandroid.data.model.IngredientWithAmountDto
import com.grig.recipesandroid.data.model.RecipeDto
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RecipeListScreen(
    viewModel: RecipesViewModel,
    navController: NavController
) {
    val recipes by viewModel.recipes.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }

    println("MY RECIPES: ${recipes}")

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        println("MY RECIPES: ${recipes.size} items")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(recipes) { recipe ->
                RecipeItem(recipe) {
                    navController.navigate("recipe_detail/${recipe.id}")
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: RecipeDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            recipe.image?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = recipe.name,
                    modifier = Modifier.size(80.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium
                )
                recipe.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeItem() {
    val samleRecipe = RecipeDto(
        id = 1L,
        name = "Шоколадный торт",
        description = "вкусный дессерт для сладкоежек",
        image = "https://via.placeholder.com/150",
        categories = listOf(CategoryDto(1, "Десерт", null)),
        ingredients = listOf(IngredientWithAmountDto(1, "Шоколад", "200", "г")),
        steps = listOf("Растопить шоколад", "Смешать с мукой", "Выпекать 30 минут")
    )
    RecipeItem(recipe = samleRecipe, onClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeListScreen() {
    val previewViewModel = PreviewRecipesViewModel()
    val navController = rememberNavController()
    RecipeListScreen(viewModel = previewViewModel, navController = navController)
}

class PreviewRecipesViewModel : RecipesViewModel(
    repository = null // мы не будем использовать реальный API
) {
    override val recipes = MutableStateFlow(
        listOf(
            RecipeDto(
                id = 1,
                name = "Шоколадный торт",
                description = "Вкусный десерт для сладкоежек",
                image = "https://via.placeholder.com/150",
                categories = listOf(CategoryDto(1, "Десерт", null)),
                ingredients = listOf(IngredientWithAmountDto(1, "Шоколад", "200", "г")),
                steps = listOf("Растопить шоколад", "Смешать с мукой", "Выпекать 30 минут")
            ),
            RecipeDto(
                id = 2,
                name = "Цезарь салат",
                description = "Свежий салат с курицей",
                image = "https://via.placeholder.com/150",
                categories = listOf(CategoryDto(2, "Салат", null)),
                ingredients = listOf(IngredientWithAmountDto(2, "Курица", "150", "г")),
                steps = listOf("Нарезать курицу", "Добавить соус", "Смешать")
            )
        )
    )
    override val loading = MutableStateFlow(false)
}
