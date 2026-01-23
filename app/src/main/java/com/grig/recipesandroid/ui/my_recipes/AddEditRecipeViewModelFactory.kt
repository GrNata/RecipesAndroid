package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

class AddEditRecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val unitRepository: UnitRepository,
    private val authViewModel: AuthViewModel,
    private val recipesViewModel: RecipesViewModel,
    private val myRecipesViewModel: MyRecipesViewModel,
    private val navController: NavController
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditRecipeViewModel::class.java)) {
            return AddEditRecipeViewModel(
                recipeRepository,
                categoryRepository,
                ingredientRepository,
                unitRepository,
                authViewModel,
                recipesViewModel,
                myRecipesViewModel,
                navController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}