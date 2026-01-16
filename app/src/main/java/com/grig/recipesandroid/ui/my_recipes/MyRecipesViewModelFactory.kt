package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.RecipeRepository

class MyRecipesViewModelFactory(
    private val recipeRepository: RecipeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyRecipesViewModul::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyRecipesViewModul(recipeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}