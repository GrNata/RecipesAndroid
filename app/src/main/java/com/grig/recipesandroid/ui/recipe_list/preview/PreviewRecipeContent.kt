package com.grig.recipesandroid.ui.recipe_list.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.domain.model.Ingredient
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.RecipeIngredient
import com.grig.recipesandroid.ui.recipe_list.RecipeItem

@Preview(showBackground = true)
@Composable
fun PreviewRecipeListContent() {
    val previewRecipes = listOf(
        Recipe(
            id = 1,
            name = "Шоколадный торт",
            description = "Нежный десерт",
            image = "https://via.placeholder.com/150",
            categories = listOf(Category(1, "Десерт", null)),
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = Ingredient(1, "Шоколад"),
                    amount = "200",
                    unit = "г"
                )
            ),
            steps = listOf("Растопить шоколад", "Выпекать 30 минут")
        ),
        Recipe(
            id = 2,
            name = "Салат Цезарь",
            description = "Свежий салат",
            image = null,
            categories = listOf(Category(2, "Салат", null)),
            ingredients = listOf(
                RecipeIngredient(
                    ingredient = Ingredient(2, "Курица"),
                    amount = "150",
                    unit = "г"
                )
            ),
            steps = listOf("Нарезать ингредиенты", "Смешать с соусом")
        )
    )

    RecipeListContentPreview(
        recipes = previewRecipes,
        onRecipeClick = {}
    )
}

// Вспомогательный Composable для Preview
@Composable
fun RecipeListContentPreview(
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