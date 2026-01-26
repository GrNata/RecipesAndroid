package com.grig.recipesandroid.ui.search_by_ingredients

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.model.request.SearchByIngredientsRequest
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.launch

class SearchByIngredientsViewModel(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

//      ++++++++++++++++++++++++++++++++++++++++++++++++
    //    +++++++      Поиск рецептов по ингредиентам
//  ++++++++++++++++++++++++++++++++++++++++++++++++

    val selectedIngredientIds = mutableStateListOf<Long>()

    private val _searchRecipes = mutableStateOf<List<Recipe>>(emptyList())
    val searchRecipes: State<List<Recipe>> = _searchRecipes

    var errorMessage by mutableStateOf<String?>(null)
        private set



    //    Методы управления ингредиентами
    fun toggleIngredient(ingredientId: Long) {
        if (selectedIngredientIds.contains(ingredientId)) {
            selectedIngredientIds.remove(ingredientId)
        } else {
            if (selectedIngredientIds.size >= 10) {
                errorMessage = "Можно выбрать максимум 10 ингредиентов"
                return
            }
            selectedIngredientIds.add(ingredientId)
        }
    }

    fun clearingredientsSelection() {
        selectedIngredientIds.clear()
    }

    //    Поиск рецептов по ингредиентам
    fun searchRecipesByIngredients() {
        viewModelScope.launch {
            try {
                Log.d("SEARCH INGREDIENT", "SearchByIngredientViewModel: START - searchRecipesByIngredients()")
                val request = SearchByIngredientsRequest(
                    ingredientIds = selectedIngredientIds.toList()
                )
                Log.d("SEARCH INGREDIENT", "SearchByIngredientViewModel: selectedIngredientIds: ${selectedIngredientIds.toList()}")
                Log.d("SEARCH INGREDIENT", "SearchByIngredientViewModel: request: ${request}")
                val result = recipeRepository.searchRecipesByIngredients(request)
                _searchRecipes.value = result.map { it.toDomain() }
                Log.d("SEARCH INGREDIENT", "SearchByIngredientViewModel: _searchRecipes.value: ${_searchRecipes.value}")
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
//    +++++++++++++++++++++++++++++++++++++++++

////    Получение словаря ингредиентов
//    fun getAllIngredients() {
//        viewModelScope.launch {
//            try {
//                val request= ingredientRepository.getAllIngredients()
//                Log.d("SEARCH INGREDIENT", "SearchByIngredientsViewModel: ingredients: ${request}")
//                ingredientsDictionary = request
//            } catch (e: Exception) {
//                errorMessage = e.message
//            }
//        }
//}
}