package com.grig.recipesandroid.ui.recipe_detail.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.domain.model.Ingredient
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.RecipeIngredient
import com.grig.recipesandroid.ui.recipe_detail.RecipeDetailContent
import com.grig.recipesandroid.ui.theme.RecipesAndroidTheme

// Фейковые данные
private val previewRecipe = Recipe(
    id = 1,
    name = "Оливье",
    description = "Классический новогодний салат",
    image = "https://img.freepik.com/premium-psd/traditional-russian-salad-olivier-transparent-background_1269588-9267.jpg?semt=ais_hybrid",
    categories = listOf(Category(1, "Салаты", null)),
    ingredients = listOf(
        RecipeIngredient(
            Ingredient(1, "Картофель"),
            amount = "2",
            unit = "шт"
        ),
        RecipeIngredient(
            Ingredient(2, "Яйца"),
            amount = "3",
            unit = "шт"
        )
    ),
    steps = listOf(
        "Отварить ингредиенты",
        "Нарезать кубиками",
        "Заправить майонезом"
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewRecipeDetailContent() {
    RecipesAndroidTheme {
        RecipeDetailContent(
            recipe = previewRecipe,
            loading = false,
            error = null,
            onBack = {}
        )
    }
}