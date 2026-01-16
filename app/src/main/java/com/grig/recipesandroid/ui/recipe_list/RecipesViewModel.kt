package com.grig.recipesandroid.ui.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
//        favoritesRepository.localFavoritesFlow(userIdFlow)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )



    private val _lastToggledRecipeId = MutableStateFlow<Long?>(null)
    val lastToggleRecipeId: StateFlow<Long?> = _lastToggledRecipeId

//    private val _favorites = MutableStateFlow<Set<Long>>(emptySet())
//    val favorites: StateFlow<Set<Long>> = _favorites

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

////    Загрузка избранных при инициализации
//    init {
//    Log.d("СЕРДЦЕ - 7", "Загрузка избранных при инициализации")
//        viewModelScope.launch {
//            try {
//                val favs = favoritesRepository.getFavorites()
////                val favsRecipeId = favs.map { it as Long}.toSet()
//                _favorites.value = favs
////                _favorites.value = favsRecipeId
//                Log.d("СЕРДЦЕ - 6", "Favorite.Value = ${favs}")
//            } catch (e: Exception) {
//                // Игнорируем ошибки, например при неавторизованном пользователе
//                _favorites.value = emptySet()
//                _messageFlow.emit("Ошибка не удалось загрузить избранное")
////                Log.d("СЕРДЦЕ - 8", "Error - Загрузка избранных при инициализации - emtySet()")
//                Log.d("СЕРДЦЕ - 8", "loadFavorites error", e)
//            }
//        }
//    }

//    fun syncFavoritesIfLoggerdIn(isAuthenticated: Boolean) {
//        if (!isAuthenticated) return
//
//        viewModelScope.launch {
//            val userId = userIdFlow.value ?: return@launch
//
//            // 1. Локальные избранные
//            val  localFavs = favoritesRepository.localFavoritesFlow(userId).first()
//
//            // 2. Серверные избранные
//            val serverFavs = try {
//                favoritesRepository.getFavoritesFromServer()
//            } catch (e: Exception) {
//                emptySet()
//            }
//
//            // 3. Объединяем их (например, server имеет приоритет)
//            val merged = serverFavs + localFavs
//
//            // 4. Сохраняем объединённые в DataStore
//            favoritesRepository.saveFavoritesToLocal(userId, merged)
//
////            try {
////                favoritesRepository.loadFromServerAndSync()
////            } catch (e: Exception) {}
////            // offline — используем локальные данные
//        }
//    }
    fun syncFavoritesIfLoggedIn(userId: String?) {
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
        viewModelScope.launch {
            val currentUserId = userIdFlow.value
            favoritesRepository.clearLocal(currentUserId)
        }
    }

    fun toggleFavorite(recipeId: Long) {
        viewModelScope.launch {
            val currentUserId = userIdFlow.value
            if (favorites.value.contains(recipeId)) {
                favoritesRepository.removeFromFavorites(recipeId, currentUserId)
            } else {
                favoritesRepository.addToFavorites(recipeId, currentUserId)
            }
        }
    }

//    Переключение избранного
//    fun toggleFavorite(recipeId: Long) {
//        viewModelScope.launch {
////            if (_favorites.value.contains(recipeId as Favorite)) {
//            if (_favorites.value.contains(recipeId)) {
//                Log.d("СЕРДЦЕ - 5", "Favorite.Value = ${_favorites.value}")
//                try {
//                    favoritesRepository.removeFromFavorites(recipeId)
//                    _favorites.value = _favorites.value - recipeId
//                } catch (e: Exception) {
//                    // обработка ошибок, например Toast
//                    _messageFlow.emit("Ошибка при удалении в избранное")
//                }
//            } else {
//                try {
//                    favoritesRepository.addToFavorites(recipeId)
//                    _favorites.value = _favorites.value + recipeId
//                } catch (e: Exception) {
//                    // обработка ошибок, например Toast
//                    _messageFlow.emit("Ошибка при добавлении в избранное")
//                }
//            }
//            // Обновляем последний изменённый рецепт
//            _lastToggledRecipeId.value = recipeId
//        }
//    }

////    для подгрузки favorite для вновь залогиненого
//    fun loadFavorites() {
//        viewModelScope.launch {
//            try {
//                val favs = favoritesRepository.getFavorites()
//                _favorites.value = favs
//                Log.d("СЕРДЦЕ - 67", "Favorite.Value = ${favs}")
//            } catch (e: Exception) {
//                _favorites.value = emptySet()
//                _messageFlow.emit("Ошибка не удалось загрузить избранное")
////                Log.d("СЕРДЦЕ - 68", "Error - Загрузка избранных при инициализации - emtySet()")
//                Log.d("СЕРДЦЕ - 68", "loadFavorites error", e)
//            }
//        }
//    }

//    fun clearFavorites() {
//        _favorites.value = emptySet()
//    }

//Paging уже умеет повторять загрузку через метод retry() у PagingData
    fun retryRecipes() {
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