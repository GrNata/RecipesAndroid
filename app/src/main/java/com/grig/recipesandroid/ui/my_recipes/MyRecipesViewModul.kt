package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyRecipesViewModul(
    private val repository: RecipeRepository
) : ViewModel() {

//    private val _myRecipes = MutableStateFlow<List<RecipeDto>>(emptyList())
//    val myRecipes: StateFlow<List<RecipeDto>> = _myRecipes
//
//    private val _messageFlow = MutableStateFlow<String>("")
//    val messageFlow: SharedFlow<String> = _messageFlow

    val myRecipesPagingFlow = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { MyRecipesPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

//    private val _loading = MutableStateFlow(false)
//    val loading: StateFlow<Boolean> = _loading
//
//    fun loadMyRecipes() {
//        viewModelScope.launch {
//            _loading.value = true
//            try {
//                val response = repository.getMyRecipes()
//                _loading.value = true
//            } catch (e: Exception) {
//                _myRecipes.value = emptyList()
//                // Отлавливаем ошибки Paging
//                _messageFlow.emit("У Вас нет рецептов или Ошибка загрузки рецептов: ${e.localizedMessage}")
//            } finally {
//                _loading.value = false
//            }
//        }
//    }
}