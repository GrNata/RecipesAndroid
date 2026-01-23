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
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    // Преобразуем Flow<String?> → StateFlow<String?> прямо здесь
    private val accessTokenState: StateFlow<String?> = authViewModel.accessToken
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val myRecipesPagingFlow = refreshTrigger
        .flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { MyRecipesPagingSource(repository) }
            ).flow
        }
        .cachedIn(viewModelScope)
//    val myRecipesPagingFlow = Pager(
//        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
////        pagingSourceFactory = { MyRecipesPagingSource(repository, accessTokenState) }
//        pagingSourceFactory = { MyRecipesPagingSource(repository) }
//    ).flow.cachedIn(viewModelScope)


    fun refresh() {
        refreshTrigger.update { it + 1 }
    }



}