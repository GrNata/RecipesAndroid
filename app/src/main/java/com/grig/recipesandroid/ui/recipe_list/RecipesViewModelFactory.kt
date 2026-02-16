package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModel
import kotlinx.coroutines.flow.StateFlow

class RecipesViewModelFactory(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val myRecipesViewModel: MyRecipesViewModel,
    private val userIdFlow: StateFlow<String?>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            Log.d("CICLE RecipesViewModelFactory", "RecipesViewModelFactory - Создание $modelClass")
            return RecipesViewModel(repository, favoritesRepository, categoryRepository, ingredientRepository, myRecipesViewModel, userIdFlow) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}