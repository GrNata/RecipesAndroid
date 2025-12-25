package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.paging.RecipePagingSource
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//  ViewModel отвечает за данные (Flow<PagingData>) и их загрузку из репозитория
open class RecipesViewModel(
    private val repository: RecipeRepository
) : ViewModel() {


    private val _query = MutableStateFlow("")       // _query — хранит текущий текст поиска
    val query: StateFlow<String> = _query               // setQuery() — вызывается при вводе в текстовое поле

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

//🔹 Никаких launch, loadRecipes, StateFlow
//🔹 Paging сам управляет загрузкой
// Paging Flow
    val recipesPagingFlow = _query
    .debounce(300)          // чтобы не фильтровать на каждый символ
    .distinctUntilChanged()
    .flatMapLatest { query ->
        repository.getRecipesPaper()
            .flow
            .map { pagingData ->
                if (query.isBlank()) {
                    pagingData
                } else {
                    pagingData.filter { recipe ->
                        recipe.name.contains(query, ignoreCase = true)
                    }
                }
//                pagingData.filter { it.name.contains(query, ignoreCase = true) }
            }
            .cachedIn(viewModelScope)
    }

//    val recipesPagingFlow =
//        repository.getRecipesPaper()
//            .flow
//            .cachedIn(viewModelScope)
}