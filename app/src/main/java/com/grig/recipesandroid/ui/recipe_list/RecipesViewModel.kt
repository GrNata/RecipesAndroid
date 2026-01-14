package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.saveable.Saver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.grig.recipesandroid.data.model.dto.Favorite
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.paging.RecipePagingSource
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Flow

//  ViewModel отвечает за данные (Flow<PagingData>) и их загрузку из репозитория
open class RecipesViewModel(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _lastToggledRecipeId = MutableStateFlow<Long?>(null)
    val lastToggleRecipeId: StateFlow<Long?> = _lastToggledRecipeId

    private val _favorites = MutableStateFlow<Set<Long>>(emptySet())
    val favorites: StateFlow<Set<Long>> = _favorites

    private val _query = MutableStateFlow("")       // _query — хранит текущий текст поиска
    val query: StateFlow<String> = _query               // setQuery() — вызывается при вводе в текстовое поле

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    private val _messageFlow = MutableStateFlow<String>("")
    val messageFlow: SharedFlow<String> = _messageFlow

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
            }
            .cachedIn(viewModelScope)

//    Загрузка избранных при инициализации
    init {
    Log.d("СЕРДЦЕ - 7", "Загрузка избранных при инициализации")
        viewModelScope.launch {
            try {
                val favs = favoritesRepository.getFavorites()
//                val favsRecipeId = favs.map { it as Long}.toSet()
                _favorites.value = favs
//                _favorites.value = favsRecipeId
                Log.d("СЕРДЦЕ - 6", "Favorite.Value = ${favs}")
            } catch (e: Exception) {
                // Игнорируем ошибки, например при неавторизованном пользователе
                _favorites.value = emptySet()
                _messageFlow.emit("Ошибка не удалось загрузить избранное")
//                Log.d("СЕРДЦЕ - 8", "Error - Загрузка избранных при инициализации - emtySet()")
                Log.d("СЕРДЦЕ - 8", "loadFavorites error", e)
            }
        }
    }

//    Переключение избранного
    fun toggleFavorite(recipeId: Long) {
        viewModelScope.launch {
//            if (_favorites.value.contains(recipeId as Favorite)) {
            if (_favorites.value.contains(recipeId)) {
                Log.d("СЕРДЦЕ - 5", "Favorite.Value = ${_favorites.value}")
                try {
                    favoritesRepository.removeFromFavorites(recipeId)
                    _favorites.value = _favorites.value - recipeId
                } catch (e: Exception) {
                    // обработка ошибок, например Toast
                    _messageFlow.emit("Ошибка при удалении в избранное")
                }
            } else {
                try {
                    favoritesRepository.addToFavorites(recipeId)
                    _favorites.value = _favorites.value + recipeId
                } catch (e: Exception) {
                    // обработка ошибок, например Toast
                    _messageFlow.emit("Ошибка при добавлении в избранное")
                }
            }
            // Обновляем последний изменённый рецепт
            _lastToggledRecipeId.value = recipeId
        }
    }

//    для подгрузки favorite для вновь залогиненого
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favs = favoritesRepository.getFavorites()
                _favorites.value = favs
                Log.d("СЕРДЦЕ - 67", "Favorite.Value = ${favs}")
            } catch (e: Exception) {
                _favorites.value = emptySet()
                _messageFlow.emit("Ошибка не удалось загрузить избранное")
//                Log.d("СЕРДЦЕ - 68", "Error - Загрузка избранных при инициализации - emtySet()")
                Log.d("СЕРДЦЕ - 68", "loadFavorites error", e)
            }
        }
    }

    fun clearFavorites() {
        _favorites.value = emptySet()
    }
}