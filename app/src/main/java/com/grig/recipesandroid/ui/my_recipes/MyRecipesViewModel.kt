package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.*

class MyRecipesViewModel(
    private val repository: RecipeRepository,
    private val authViewModel: AuthViewModel,
//    userIdFlow: StateFlow<String?>
) : ViewModel() {

//    private val _myRecipes = MutableStateFlow<List<RecipeDto>>(emptyList())
//    val myRecipes: StateFlow<List<RecipeDto>> = _myRecipes
//
//    private val _messageFlow = MutableStateFlow<String>("")
//    val messageFlow: SharedFlow<String> = _messageFlow

    // Преобразуем Flow<String?> → StateFlow<String?> прямо здесь
    private val accessTokenState: StateFlow<String?> = authViewModel.accessToken
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
    val myRecipesPagingFlow = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
//        pagingSourceFactory = { MyRecipesPagingSource(repository, accessTokenState) }
        pagingSourceFactory = { MyRecipesPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)
//    val myRecipesPagingFlow = userIdFlow.flatMapLatest { userId ->
//        repository.getMyRecipes(userId).cachedIn(viewModelScope)
//    }




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