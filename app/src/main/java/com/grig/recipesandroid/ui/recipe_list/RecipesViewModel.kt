package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class RecipesViewModel(
    private val repository: RecipeRepository?
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    open val recipes: StateFlow<List<Recipe>> = _recipes

    private val _loading = MutableStateFlow(false)
    open val loading: StateFlow<Boolean> = _loading

    fun loadRecipes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Временный вывод RAW JSON
                val response = repository?.getRecipes() // уже domain-модель
                Log.d("RecipesViewModel", "RAW JSON: $response")

                // А потом обычная загрузка через Retrofit
                _recipes.value = requireNotNull(response)
            } catch (e: Exception) {
                Log.e("RecipesViewModel", "Error loading recipes", e)
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}