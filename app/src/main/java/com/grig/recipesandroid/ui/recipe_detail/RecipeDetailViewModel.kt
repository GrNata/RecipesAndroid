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

//    +++++++++++++++++++++++++
//    для пересчета количества ингредиентов в деталях рецепта
    private val _ingredientsUi = MutableStateFlow<List<IngredientUi>>(emptyList())
    val ingredientsUi: StateFlow<List<IngredientUi>> = _ingredientsUi

    //      Режим «на N порций» (очень крутая фича)
    private var baseIngredients: List<IngredientUi> = emptyList()

    private val _currentServings = MutableStateFlow(1)
    val currentServings: StateFlow<Int> = _currentServings

//  +++++++++++++++++++++++++++++++++++++

    init {
        Log.d("MODERATOR:", "RecipeDetailViewModel: before loadRecipe recipe: ${_recipe.value}")
        loadRecipe()
        Log.d("MODERATOR:", "RecipeDetailViewModel: after loadRecipe recipe: ${_recipe.value}")
    }

    //    fun loadRecipe(id: Long) {
    fun loadRecipe() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                Log.d("MODERATOR:", "RecipeDetailViewModel: recipeId: ${recipeId}")
                val response = api.getRecipeById(requireNotNull(recipeId))
                Log.d("MODERATOR:", "RecipeDetailViewModel: response: ${response}")

                val ingredientResponse =
                    response.ingredients?.map { it.toDomain() }?.map { it.toIngredientUi() }
                        ?.toMutableList()
                        ?: emptyList()

                _recipe.value = response.toDomain()

                baseIngredients = ingredientResponse

                if (ingredientResponse != null) _ingredientsUi.value = ingredientResponse

                if (response.baseServings != null) _currentServings.value = response.baseServings


            } catch (e: Exception) {
                _error.value = e.message ?: "RecipeDetailViewModel: Ошибка загрузки рецепта"
                Log.d("MODERATOR:", "RecipeDetailViewModel: error: ${e.message}")
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
//    Подсветка базового ингредиента
    private val _baseIngredientId = MutableStateFlow<Long?>(null)
    val baseIngredientId: StateFlow<Long?> = _baseIngredientId

    private val _baseServings = MutableStateFlow(1)
    val baseServings: StateFlow<Int> = _baseServings
    fun recalculateFrom(
        ingredientId: Long,
        newAmount: Double
    )  {
        _baseIngredientId.value = ingredientId

        val base = _ingredientsUi.value.firstOrNull { it.id == ingredientId }
            ?: return
        val oldAmount = base.amount ?: return
        if (oldAmount == 0.0) return

        val factor = newAmount / oldAmount

        _ingredientsUi.value = _ingredientsUi.value.map { ingredient ->
            ingredient.copy(
                amount = ingredient.amount?.let { it * factor }
            )
        }
    }

//    Прибавление / убавление количества ингредиента (пересчет кол-ва всех ингредиентов) - стрелками + или -
    fun recalculateForServings(newServings: Int) {
        if (newServings <= 0) return

//        val factor = newServings.toDouble() / _baseServings.value
//        _baseServings.value = newServings         //  всегда пересчёт от эталона
//        val factor = newServings.toDouble() / _currentServings.value
        val factor = newServings.toDouble() / (_recipe.value?.baseServings ?: 1)
//    val factor = newServings.toDouble() / recipeBaseServings
        _currentServings.value = newServings

//        _ingredientsUi.value = _ingredientsUi.value.map {
            _ingredientsUi.value = baseIngredients.map {         //  всегда пересчёт от эталона
            it.copy(amount = it.amount?.times(factor))
//            it.copy(amount = it.amount?.let { amt -> amt * factor })
        }
    }

//      Режим «на N порций» (очень крутая фича)
//    private var baseIngredients: List<IngredientUi> = emptyList()

    fun setBaseIngredients(list: List<IngredientUi>) {
        baseIngredients = list
    }

    fun recalculateForNumberOfServings(servings: Int) {
        if (servings <= 0) return

        _ingredientsUi.value = baseIngredients.map {
            it.copy(amount = it.amount?.times(servings))
        }
    }
}