package com.grig.recipesandroid.ui.recipe_detail

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
    private val api: RecipeApi
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRecipe(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.getRecipeById(id)
                _recipe.value = response.toDomain()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки рецепта"
            } finally {
                _loading.value = false
            }
        }
    }
}