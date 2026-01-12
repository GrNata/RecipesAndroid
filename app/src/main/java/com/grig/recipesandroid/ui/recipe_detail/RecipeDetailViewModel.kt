package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val api: RecipeApi,
    private val recipeId: Long
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadRecipe()
    }

    //    fun loadRecipe(id: Long) {
    fun loadRecipe() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("ИЩУ:", " recipeId = ${recipeId}")
                val response = api.getRecipeById(recipeId)
                Log.d("ИЩУ:", " recipeId = ${recipeId}, response${response}")
                _recipe.value = response.toDomain()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки рецепта"
            } finally {
                _loading.value = false
            }
        }
    }
}