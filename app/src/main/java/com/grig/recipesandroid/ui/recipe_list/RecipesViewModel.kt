package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class RecipesViewModel(
    private val repository: RecipeRepository?
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeDto>>(emptyList())
    open val recipes: StateFlow<List<RecipeDto>> = _recipes

    private val _loading = MutableStateFlow(false)
    open val loading: StateFlow<Boolean> = _loading

    fun loadRecipes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Временный вывод RAW JSON
                val response = repository?.getRecipes() // PagedRecipesResponse
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