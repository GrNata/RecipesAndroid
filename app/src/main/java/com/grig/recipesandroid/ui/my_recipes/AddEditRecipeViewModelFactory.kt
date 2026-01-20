package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel

class AddEditRecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val unitRepository: UnitRepository,
    private val authViewModel: AuthViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditRecipeViewModel::class.java)) {
            return AddEditRecipeViewModel(recipeRepository, categoryRepository, ingredientRepository, unitRepository,  authViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}