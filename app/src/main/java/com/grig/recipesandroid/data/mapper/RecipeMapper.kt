package com.grig.recipesandroid.data.mapper

import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.IngredientWithAmountDto
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.ui.IngredientUi
import com.grig.recipesandroid.domain.model.CategoryValue
import com.grig.recipesandroid.domain.model.Ingredient
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.domain.model.RecipeIngredient

fun IngredientWithAmountDto.toDomain() : RecipeIngredient =
    RecipeIngredient(
        ingredient = Ingredient(id = id, name = name),
        amount = amount,
//        unit = unit?.label ?: ""
        unit = unit
    )


fun RecipeDto.toDomain() : Recipe =
    Recipe(
        id = id ?: 0L,
        name = name,
        description = description,
        image = image,
        baseServings = baseServings,
        categories = categories
            ?.values
            ?.map { it.toDomain() }
            ?: emptyList(),
        ingredients = ingredients?.map { it.toDomain() } ?: emptyList(),
        steps = (steps ?: emptyList())
    )

fun CategoryValueDto.toDomain(): CategoryValue =
    CategoryValue(
        id = id,
        categoryTypeId = typeId,
        categoryTypeName = typeName,
        categoryValue = categoryValue
    )

fun RecipeIngredient.toIngredientUi() : IngredientUi =
    IngredientUi(
        id = ingredient.id,
        name = ingredient.name,
        amount = amount?.toDoubleOrNull(),
        unit = unit?.label
    )


