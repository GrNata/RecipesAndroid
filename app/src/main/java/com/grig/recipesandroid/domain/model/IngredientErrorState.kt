package com.grig.recipesandroid.domain.model

//  Модель ошибки
//  Что считать ошибкой
//	•	ингредиент выбран, но:
//	•	amount пустой / не число
//	•	unit == null

data class IngredientErrorState(
    val amountError: Boolean = false,
    val unitError: Boolean = false
)
