package com.grig.recipesandroid.ui.recipe_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.repository.RecipeRepository

class RecipeDetailViewModelFactory(
    private val api: RecipeApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailViewModel(api) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }
}