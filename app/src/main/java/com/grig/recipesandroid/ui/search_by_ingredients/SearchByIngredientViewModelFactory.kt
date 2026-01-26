package com.grig.recipesandroid.ui.search_by_ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository

class SearchByIngredientViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchByIngredientsViewModel::class.java)) {
            return SearchByIngredientsViewModel(recipeRepository, ingredientRepository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}