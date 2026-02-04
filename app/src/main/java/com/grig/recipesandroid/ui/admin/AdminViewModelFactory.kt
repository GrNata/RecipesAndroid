package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

class AdminViewModelFactory(
    private val authRepository: AuthRepository,
    private val ingredientRepository: IngredientRepository,
    private val recipesViewModel: RecipesViewModel,
    private val navController: NavController
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
//            Log.d("CICLE AdminViewModelFactory", "RecipesViewModelFactory - Создание $modelClass")
            return AdminViewModel(authRepository, ingredientRepository, recipesViewModel, navController) as T
        }
        throw IllegalArgumentException("Uknown ViewModel class: $modelClass")
    }
}