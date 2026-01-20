package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.dto_request.RecipeDto
import com.grig.recipesandroid.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//  ViewModel отвечает за данные (Flow<PagingData>) и их загрузку из репозитория
open class RecipesViewModel(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository,
    private val userIdFlow: StateFlow<String?>      // сюда передаем текущий userId / email
) : ViewModel() {

//    теперь избранное — offline-first,
    val favorites: StateFlow<Set<Long>> =
        userIdFlow
            .flatMapLatest { userId ->
                favoritesRepository.localFavoritesFlow(userId)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

    private val _lastToggledRecipeId = MutableStateFlow<Long?>(null)
    val lastToggleRecipeId: StateFlow<Long?> = _lastToggledRecipeId

    private val _query = MutableStateFlow("")       // _query — хранит текущий текст поиска
    val query: StateFlow<String> = _query               // setQuery() — вызывается при вводе в текстовое поле

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    private val _messageFlow = MutableStateFlow<String>("")
    val messageFlow: SharedFlow<String> = _messageFlow

//    Теперь добавим объект Pager с Retry. Paging уже умеет повторять загрузку через метод retry() у PagingData. Нам нужно просто сохранить Flow, чтобы в UI можно было вызвать повтор
    lateinit var lastRecipesPagingFlow: PagingData<RecipeDto>

//    Поиск
//🔹 Никаких launch, loadRecipes, StateFlow
//🔹 Paging сам управляет загрузкой
// Flow с debounce и фильтрацией в PagingSource
    val recipesPagingFlow = _query
        .debounce(300)          // чтобы не фильтровать на каждый символ - ждем 300ms после последнего ввода
        .distinctUntilChanged()             // пропускаем повторные значения
        .flatMapLatest { q ->
            repository.getRecipesPaper(query = q)       // передаем query в Pager/PagingSource
                .flow
                .catch { e ->
                    // Отлавливаем ошибки Paging
                    _messageFlow.emit("Ошибка загрузки рецептов: ${e.localizedMessage}")
                    }
                }
                .cachedIn(viewModelScope)


    private var favoritesSyncedForUser: String? = null

    init {
        Log.d("CICLE RecipeViewModel", "RecipeViewModel - init")
        viewModelScope.launch {
            userIdFlow
                .collect { userId ->
                    when {
                        userId == null -> {
                            favoritesSyncedForUser = null
                            clearFavorites()
                        }

                        favoritesSyncedForUser != userId -> {
                            favoritesSyncedForUser = userId
                            syncFavoritesIfLoggedIn(userId)
                        }
                    }
                }
        }
    }


    fun syncFavoritesIfLoggedIn(userId: String?) {
        Log.d("CICLE RecipeViewModel", "RecipeViewModel - syncFavoritesIfLoggedIn")
        viewModelScope.launch {
            if (userId == null) return@launch

            try {
                favoritesRepository.syncFavoritesWithServer(userId)
            } catch (e: Exception) {
                _messageFlow.emit("Не удалось синхронизовать избранное")
            }
        }
    }

    fun clearFavorites() {
        Log.d("CICLE RecipeViewModel", "RecipeViewModel - clearFavorites")
        viewModelScope.launch {
            val currentUserId = userIdFlow.value
            favoritesRepository.clearLocal(currentUserId)
        }
    }

    fun toggleFavorite(recipeId: Long) {
        Log.d("CICLE RecipeViewModel", "RecipeViewModel - toggleFavorite")
        viewModelScope.launch {
            val currentUserId = userIdFlow.value
            if (favorites.value.contains(recipeId)) {
                favoritesRepository.removeFromFavorites(recipeId, currentUserId)
            } else {
                favoritesRepository.addToFavorites(recipeId, currentUserId)
            }
        }
    }


//Paging уже умеет повторять загрузку через метод retry() у PagingData
    fun retryRecipes() {
    Log.d("CICLE RecipeViewModel", "RecipeViewModel - retryRecipes")
        // У PagingData есть extension функция retry()
        viewModelScope.launch {
            try {
                // Пробуем перезапустить загрузку текущего Flow
                recipesPagingFlow.retry()
            } catch (e: Exception) {
                _messageFlow.emit("Не удалось повторить загрузку: ${e.localizedMessage}")
            }
        }
    }
}