package com.grig.recipesandroid.data.mapper

import com.grig.recipesandroid.data.model.dto.IngredientWithAmountDto
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.domain.model.Ingredient
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.RecipeIngredient

fun IngredientWithAmountDto.toDomain() : RecipeIngredient =
    RecipeIngredient(
        ingredient = Ingredient(id = id, name = name),
        amount = amount,
        unit = unit?.label ?: ""
    )


fun RecipeDto.toDomain() : Recipe =
    Recipe(
        id = id ?: 0L,
        name = name,
        description = description,
        image = image,
        categories = categories?.map { Category(it.id ?: 0L, it.name, it.image) } ?: emptyList(),
        ingredients = ingredients?.map { it.toDomain() } ?: emptyList(),
        steps = steps ?: emptyList()
    )