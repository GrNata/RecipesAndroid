package com.grig.recipesandroid.data.mapper

import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.IngredientWithAmountDto
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.domain.model.Category
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
        categoryValueIds = categoryValues?.map {  cv ->
            CategoryValue(
                id = cv.id,
                categoryTypeId = cv.typeId,
                categoryTypeName = cv.typeName,
                categoryValue = cv.categoryValue ?: ""
            )
//            CategoryValueDto(
//                id = cv.id,
//                typeId = cv.typeId,
//                typeName = cv.typeName,
//                categoryValue = cv.categoryValue ?: ""
//            )
        } ?: emptyList(),
        ingredients = ingredients?.map { it.toDomain() } ?: emptyList(),
        steps = (steps ?: emptyList())
    )

//fun Recipe.toDto(): RecipeDto =
//    RecipeDto(
//        id = id ?: 0L,
//        name = name,
//        description = description,
//        image = image,
//        categoryValues = categoryValueIds?.map { cv ->
//                        CategoryValueDto(
//                id = cv.id,
//                typeId = cv.categoryTypeId,
//                typeName = cv.categoryTypeName,
//                categoryValue = cv.categoryValue
//            )
//        } ?: emptyList(),
//        ingredients = ingredients?.map { ing ->
//            IngredientWithAmountDto(
//                id = ing.id
//            )
//        },
//        steps = steps
//    )

