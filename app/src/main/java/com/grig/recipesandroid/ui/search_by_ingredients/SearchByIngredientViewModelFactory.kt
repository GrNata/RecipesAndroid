package com.grig.recipesandroid.ui.search_by_ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

class SearchByIngredientViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val recipesViewModel: RecipesViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchByIngredientsViewModel::class.java)) {
            return SearchByIngredientsViewModel(recipeRepository, ingredientRepository, recipesViewModel ) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}