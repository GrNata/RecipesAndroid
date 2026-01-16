package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import kotlinx.coroutines.flow.StateFlow

class RecipesViewModelFactory(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository,
    private val userIdFlow: StateFlow<String?>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
            Log.d("VM_DEBUG", "Создание $modelClass")
            return RecipesViewModel(repository, favoritesRepository, userIdFlow) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}