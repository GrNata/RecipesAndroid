package com.grig.recipesandroid.domain.model

import com.grig.recipesandroid.data.model.dto_request.UnitDto

//  Связь рецепт ↔ ингредиент

//	•	Для UI (просмотр, список, детали) используем RecipeIngredientUi с String?.
//	•	Для редактирования/создания используем RecipeIngredient с UnitDto?.
//	•	Конвертация через toUi() перед передачей в Compose.

//  Для Add/Edit — оставляем RecipeIngredient с UnitDto?, чтобы форма работала
data class RecipeIngredient(
    val ingredient: Ingredient,
    val amount: String?,
//    val unit: String?
    val unit: UnitDto?
)

//      Для экрана деталей и MyRecipesScreen — использовать RecipeIngredientUi, где unit: String?

data class RecipeIngredientUi(
    val ingredient: Ingredient,
    val amount: String?,
    val unit: String?
)

fun RecipeIngredient.toUi(): RecipeIngredientUi =
    RecipeIngredientUi(
        ingredient = ingredient,
        amount = amount,
        unit = unit?.label ?: ""
    )
