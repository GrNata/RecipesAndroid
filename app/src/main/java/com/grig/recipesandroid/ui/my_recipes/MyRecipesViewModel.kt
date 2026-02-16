package com.grig.recipesandroid.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.model.dto.RecipeStatus
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyRecipesViewModel(
    private val repository: RecipeRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow(0)

    // Триггер обновления при изменении токена (логин/логаут)
    private val tokenRefreshTrigger = MutableStateFlow(0)


    // Преобразуем Flow<String?> → StateFlow<String?> прямо здесь
    private val accessTokenState: StateFlow<String?> = authViewModel.accessToken
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    init {
//        Если токен изменился (логин/аккаунт) - обновляем список
        viewModelScope.launch {
            accessTokenState.collect {
                tokenRefreshTrigger.update { it + 1 }
            }
        }
    }

    val myRecipesPagingFlow = combine(refreshTrigger, tokenRefreshTrigger) { r, t ->
        r to t
    }
//        refreshTrigger
        .flatMapLatest {
            Pager(
                config = PagingConfig(pageSize = 10, enablePlaceholders = false),
                pagingSourceFactory = { MyRecipesPagingSource(repository) }
            ).flow
        }
        .cachedIn(viewModelScope)

    fun refresh() {
        refreshTrigger.update { it + 1 }
    }



}