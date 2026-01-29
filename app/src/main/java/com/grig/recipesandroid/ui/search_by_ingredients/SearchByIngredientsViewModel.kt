package com.grig.recipesandroid.ui.search_by_ingredients

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.data.model.request.SearchByIngredientsRequest
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchByIngredientsViewModel(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val recipesViewModel: RecipesViewModel
) : ViewModel() {

//    var ingredientsAll by mutableStateOf<List<IngredientDto>>(emptyList())
//    var ingredientsAll = mutableStateListOf<IngredientDto>()
//    var ingredientsAll: IngredientRepository by mutableStateOf<List<IngredientDto>>(emptyList())
//    var ingredientsAll: IngredientRepository by mutableStateOf<List<IngredientDto>>(emptyList())
//    private set

//      ++++++++++++++++++++++++++++++++++++++++++++++++
    //    +++++++      Поиск рецептов по ингредиентам
//  ++++++++++++++++++++++++++++++++++++++++++++++++
// список ID выбранных ингредиентов
    val selectedIngredientIds = mutableStateListOf<Long>()
//  список найденных рецептов
    private val _searchRecipes = mutableStateOf<List<Recipe>>(emptyList())
    val searchRecipes: State<List<Recipe>> = _searchRecipes

    var errorMessage by mutableStateOf<String?>(null)
        private set

//    ++++++++++++++++++++++++++++++++++
//    Список выбранных ингредиентов
    val selectedIngredients = mutableStateListOf<IngredientDto>()
    // Текущий ввод в поиске
    private val searchQueryIngredient = mutableStateOf<String>("")
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


//    Отфильтрованный список для отображения
//    val filteredIngredients: StateFlow<List<IngredientDto>> = _searchQuery.map { query ->
//    Log.d("Search Ingredient", "SearchByIngredientViewModel: query: $query")
//        if (query.isEmpty()) {
//            emptyList()
//        } else {
//            ingredientsAll.filter { it.name.contains(query, ignoreCase = true) }
//        }
//    }
//    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _filteredIngredients = mutableStateOf<List<IngredientDto>>(emptyList())
    var filteredIngredients: State<List<IngredientDto>> = _filteredIngredients

    // Действия
    fun changeFilteredIngredientsByQuery(searchQuery: String, ingredientsAll: List<IngredientDto>) {
            if (searchQuery.isEmpty()) {
                _filteredIngredients.value = emptyList()
            } else {
                val choiceIngredient = ingredientsAll.filter { it.name.contains(searchQuery, ignoreCase = true) }
//                _filteredIngredients.value = emptyList()
                _filteredIngredients.value = choiceIngredient

            }
    }

    fun setSearchQuery(query: String, ingredientsAll: List<IngredientDto>) {
        _searchQuery.value = query

        changeFilteredIngredientsByQuery(_searchQuery.value, ingredientsAll)
    }

    fun addIngredient(ingredient: IngredientDto) {
        if (!selectedIngredientIds.contains(ingredient.id)) {
            selectedIngredients.add(ingredient)
            selectedIngredientIds.add(ingredient.id)
        }
    }

    fun removeIngredient(ingredient: IngredientDto) {
        selectedIngredients.remove(ingredient)
        selectedIngredientIds.remove(ingredient.id)
    }

//    ++++++++++++++++++++++++++++++++++


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
                val request = SearchByIngredientsRequest(
                    ingredientIds = selectedIngredientIds.toList()
                )
                val result = recipeRepository.searchRecipesByIngredients(request)
                _searchRecipes.value = result.map { it.toDomain() }
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}