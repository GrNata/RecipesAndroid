package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.mapper.toDomain
import com.grig.recipesandroid.data.mapper.toIngredientUi
import com.grig.recipesandroid.data.model.ui.IngredientUi
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val api: RecipeApi,
    private val recipeId: Long
) : ViewModel() {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

//    для пересчета количества ингредиентов в деталях рецепта
//    private val _ingredientsUi = MutableStateFlow<MutableList<IngredientUi>>(mutableListOf())
//    val ingredientsUi: StateFlow<MutableList<IngredientUi>> = _ingredientsUi
    private val _ingredientsUi = MutableStateFlow<List<IngredientUi>>(emptyList())
    val ingredientsUi: StateFlow<List<IngredientUi>> = _ingredientsUi

    init {
        loadRecipe()
    }

    //    fun loadRecipe(id: Long) {
    fun loadRecipe() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
//                Log.d("2-ИЩУ:", "RecipeDetailViewModel: recipeId = ${recipeId}")
                val response = api.getRecipeById(requireNotNull(recipeId))
                val ingredientResponse =
                    response.ingredients?.map { it.toDomain() }?.map { it.toIngredientUi() }
                        ?.toMutableList()
                Log.d("INGREDIENT-UI", "RecipeDetailViewModel: ingredientResponse = ${ingredientResponse}")
                _recipe.value = response.toDomain()
                if (ingredientResponse != null) {
                    _ingredientsUi.value = ingredientResponse
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "RecipeDetailViewModel: Ошибка загрузки рецепта"
            } finally {
                _loading.value = false
            }
        }
    }

//    fun updateIngredientsUi() {
//        val currentList = _ingredientsUi.value
//    }

//   ++++++++++++++++++
//  +++++++   для пересчета количества ингредиентов в деталях рецепта
//    fun setIngredientsUi(list: List<IngredientUi>) {
//        _ingredientsUi.clear()
//        _ingredientsUi.addAll(list)
//    }

    fun recalculateFrom(
        ingredientId: Long,
        newAmount: Double
    )  {
        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  ingredientsUi-1: ${ingredientsUi}")
        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  _ingredientsUi-1.value: ${_ingredientsUi.value}")
//        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  ingredientsUi2-1: ${ingredientsUi2}")
        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  newAmount: ${newAmount}")
        val base = _ingredientsUi.value.firstOrNull { it.id == ingredientId }
//        val base = ingredientsUi2.firstOrNull { it.id == ingredientId }
            ?: return
        val oldAmount = base.amount ?: return
        if (oldAmount == 0.0) return

        val factor = newAmount / oldAmount
        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  factor: ${factor}")

//        _ingredientsUi.value.replaceAll { ingredient ->
////            ingredientsUi2.replaceAll {  ingredient ->
//            ingredient.copy(
//                amount = ingredient.amount?.let { it * factor }
//            )
        _ingredientsUi.value = _ingredientsUi.value.map { ingredient ->
            ingredient.copy(
                amount = ingredient.amount?.let { it * factor }
            )
        }
//                Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  ingredient.amount: ${ingredient.amount}")
//        }
//        Log.d("INGREDIENT-UI", "RcipeDetailViewmodel: recalculateFrom:  ingredientsUi2-2: ${ingredientsUi}")

    }
}