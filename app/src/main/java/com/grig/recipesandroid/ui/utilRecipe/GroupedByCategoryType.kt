package com.grig.recipesandroid.ui.utilRecipe

import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.domain.model.Recipe

fun GroupedByCategoryType(
    recipes: List<Recipe>,
    selectedCategoryTypeId: Long
) : Map<String, List<Recipe>> {

    //            Группируем рецепты по первой категории (можно доработать для нескольких)
    val grouped = recipes
        .flatMap { recipe ->
            recipe.categories
                .filter { it.categoryTypeId == selectedCategoryTypeId }
//                                .filter { it.categoryTypeId == 1L }
                .map { it.categoryValue to recipe }       // создаём пары category -> recipe
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )

    return grouped
}