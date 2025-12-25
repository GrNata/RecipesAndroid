package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.domain.model.Ingredient
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.RecipeIngredient

//   разделяем UI и state - RecipeListContent
// RecipeListScreen получает ViewModel и передаёт данные в RecipeListContent.
@Composable
fun RecipeListScreen(
    viewModel: RecipesViewModel,
    navController: NavController
) {
    val recipes = viewModel.recipesPagingFlow.collectAsLazyPagingItems()

    RecipeListContent(
        recipes = recipes,
        onRecipeClick = { id ->
            navController.navigate("recipe_detail/${id}")
        }
    )
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row {
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
            Spacer(modifier = Modifier.padding(4.dp))
            Column {
                recipe.ingredients.forEach { ing ->
                    Text("${ing.ingredient.name}: ${ing.amount} ${ing.unit ?: ""}".trim())
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text("STEPS:")
            recipe.steps.forEach { step ->
                Text("-$step")
            }
        }
    }
}

// -----------  PREVIEW -----------------------

//    Для Preview
@Composable
fun RecipeListContent(
    recipes: List<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            RecipeItem(recipe = recipe) {
                onRecipeClick(recipe)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewRecipeListContent() {
    val previewRecipes = listOf(
        Recipe(
            id = 1,
            name = "Шоколадный торт",
            description = "Вкусный десерт",
            image = "https://via.placeholder.com/150",
            categories = listOf(Category(1, "Десерт", null)),
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = Ingredient(1L, "Шоколад"),
                    amount = "200",
                    unit = "г"
                )
            ),
            steps = listOf("Растопить шоколад", "Выпекать 30 минут")
        ),
        Recipe(
            id = 2,
            name = "Цезарь",
            description = "Свежий салат",
            image = null,
            categories = listOf(Category(2, "Салат", null)),
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = Ingredient(2L, "Курица"),
                    amount = "150",
                    unit = "г"
                )
            ),
            steps = listOf("Нарезать", "Смешать")
        )
    )

    RecipeListContent(
        recipes = previewRecipes,
        onRecipeClick = {}
    )
}

//Preview — С КАРТИНКОЙ
@Preview(showBackground = true)
@Composable
fun PreviewRecipeItemWithImage() {
    val recipe = Recipe(
        id = 1L,
        name = "Шоколадный торт",
        description = "Нежный и очень вкусный десерт",
        image = "https://via.placeholder.com/300",
        categories = listOf(
            Category(1, "Десерт", null)
        ),
        ingredients = listOf(
            RecipeIngredient(
                ingredient = Ingredient(1, "Шоколад"),
                amount = "200",
                unit = "г"
            ),
            RecipeIngredient(
                ingredient = Ingredient(2, "Мука"),
                amount = "150",
                unit = "г"
            )
        ),
        steps = listOf(
            "Растопить шоколад",
            "Смешать ингредиенты",
            "Выпекать 30 минут"
        )
    )
    RecipeItem(
        recipe = recipe,
        onClick = {}
    )
}

//  Preview — БЕЗ КАРТИНКИ
@Preview(showBackground = true)
@Composable
fun PreviewRecipeItemWithoutImage() {
    val recipe = Recipe(
        id = 2,
        name = "Салат Цезарь",
        description = "Классический салат",
        image = null, // 👈 важно
        categories = listOf(
            Category(2, "Салат", null)
        ),
        ingredients = listOf(
            RecipeIngredient(
                ingredient = Ingredient(3, "Курица"),
                amount = "150",
                unit = "г"
            ),
            RecipeIngredient(
                ingredient = Ingredient(4, "Салат"),
                amount = "1",
                unit = "шт"
            )
        ),
        steps = listOf(
            "Нарезать ингредиенты",
            "Смешать с соусом"
        )
    )
    RecipeItem(
        recipe = recipe,
        onClick = {}
    )
}
