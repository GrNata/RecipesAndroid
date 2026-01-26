package com.grig.recipesandroid.data.model.request

//  Поиск рецептов по ингредиентам
data class SearchByIngredientsRequest(
    val ingredientIds: List<Long>
)
