package com.grig.recipesandroid.ui.recipe_list

import android.net.http.HttpException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.model.dto.CategoryTypeRequest
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto
import com.grig.recipesandroid.data.model.dto.CategoryTypeUpdate
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.data.model.dto.RecipeDto
import com.grig.recipesandroid.data.model.dto.RecipeStatus
import com.grig.recipesandroid.data.model.response.PagedRecipesResponse
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.domain.model.Recipe
import com.grig.recipesandroid.ui.my_recipes.MyRecipesViewModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlin.collections.emptyList

//  ViewModel отвечает за данные (Flow<PagingData>) и их загрузку из репозитория
open class RecipesViewModel(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val myRecipesViewModel: MyRecipesViewModel,
    private val userIdFlow: StateFlow<String?>      // сюда передаем текущий userId / email
) : ViewModel() {

//    Любое изменение данных → invalidate()
    private val refreshTrigger = MutableStateFlow(0)

    fun refreshRecipe() {
        refreshTrigger.update { it + 1 }
    }

    val selectedCategoryValues = mutableMapOf<Long, CategoryValueDto>()
// key = typeId

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

    //    +++  справочник типов категорий, ингредиентов
    var categoryTypesAll by mutableStateOf<List<CategoryTypeDto>>(emptyList())
    var categoryValuesAll by mutableStateOf<List<CategoryValueDto>>(emptyList())
    var ingredientsDictionary by mutableStateOf<List<IngredientDto>>(emptyList())

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    private val _messageFlow = MutableStateFlow<String>("")
    val messageFlow: SharedFlow<String> = _messageFlow

    var errorMessage by mutableStateOf<String?>(null)
        private set

//    MODERATOR
    private val _pendingRecipes = MutableStateFlow<PagedRecipesResponse?>(null)
    val pendingRecipes = _pendingRecipes.asStateFlow()

    private val _moderatorLoading = MutableStateFlow(false)
    val moderatorLoading = _moderatorLoading.asStateFlow()

    private val _moderatorError = MutableStateFlow<String?>(null)
    val moderatorError = _moderatorError.asStateFlow()

////    Теперь добавим объект Pager с Retry. Paging уже умеет повторять загрузку через метод retry() у PagingData. Нам нужно просто сохранить Flow, чтобы в UI можно было вызвать повтор
//    lateinit var lastRecipesPagingFlow: PagingData<RecipeDto>

//    Поиск
//🔹 Никаких launch, loadRecipes, StateFlow
//🔹 Paging сам управляет загрузкой
// Flow с debounce и фильтрацией в PagingSource
//    val recipesPagingFlow: Flow<PagingData<RecipeDto>> =
    val recipesPagingFlow: Flow<PagingData<Recipe>> =
        combine(
            refreshTrigger, //  / <--- Вот он слушает изменения
            _query.debounce(300).distinctUntilChanged()
        ) { _, query -> query }
            .flatMapLatest { query ->
                // При изменении refreshTrigger этот блок выполнится заново
                // и создаст новый PagingSource, который загрузит свежие данные с сервера
                repository.getRecipesPaper(query = query)
                    .catch { e ->
                        _messageFlow.emit("Ошибка загрузки рецептов: ${e.localizedMessage}")
                    }
            }
            .cachedIn(viewModelScope)

    private var favoritesSyncedForUser: String? = null

    //    +++++++++++++++
//    MODERATION
    private val _moderationStatus = MutableStateFlow<RecipeStatus>(RecipeStatus.DRAFT)
    val moderationStatus = _moderationStatus.asStateFlow()

    private var _isModeratorDetail = MutableStateFlow<Boolean>(false)
    var isModeratorDetail = _isModeratorDetail.asStateFlow()

    private var _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

//    +++++++++++++++

    init {
        Log.d("SEARCH INGREDIENT", "RecipeViewModel - init ")
        viewModelScope.launch {
//            загрузка справочника CategotyType и Ingredientdto
            categoryTypesAll = categoryRepository.getCategoryTypes()
            Log.d("SEARCH INGREDIENT", "RecipeViewModel - init categoryTypesAll: ${categoryTypesAll}")
            categoryValuesAll = categoryRepository.getCategoryValues()
            ingredientsDictionary = ingredientRepository.getAllIngredients()
            Log.d("SEARCH INGREDIENT", "RecipesViewModel: init ingredients: ${ingredientsDictionary}")

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
                            Log.d("SEARCH INGREDIENT", "RecipeViewModel - init favoritesSyncedForUser: ${favoritesSyncedForUser}")
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


//    +++++++++++++++++++
//    Удаление рецепта
    fun deleteRecipe(recipeId: Long) {
        _error.value = null
        viewModelScope.launch {
            try {
                val response = repository.deleteRecipe(recipeId)
                if (response.isSuccessful) {
                   refreshRecipe()
                } else {
                    when (response.code()) {
                        403 -> _error.value = "Нет прав на это действие"
                        401 -> _error.value = "Нет авторизации"
                        404 -> _error.value = "Нет такого рецепта"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

//    +++++++++++++++++++
//    СПРАВОЧНИКИ - категорий и ингредиентов
//    ________________
    fun refreshCategoryType() {
        viewModelScope.launch {
            categoryTypesAll = categoryRepository.getCategoryTypes()
        }
    }
    fun refreshCategoryValues() {
        viewModelScope.launch {
            categoryValuesAll = categoryRepository.getCategoryValues()
        }
    }

    fun refreshIngredients() {
        viewModelScope.launch {
            ingredientsDictionary = ingredientRepository.getAllIngredients()
        }
    }
//    +++++++++++++++++++++++++++++++++++++
//    MODERATION
//    Отправить на модерацию
    suspend fun sendToModeration(recipeId: Long): Boolean {
            return try {
                repository.sendToModeration(recipeId)
//                оповещаем пользователя (опционно)
                _messageFlow.emit("Рецепт отправлен на проверку")
                true
            } catch (e: Exception) {
//                errorMessage = e.message
                _messageFlow.emit("Ошибка: ${e.message}")
                false
                // Если произошла ошибка, статус на сервере не поменялся.
                // В идеале тут можно послать сигнал в UI вернуть цвет обратно в серый,
                // но для начала можно оставить просто уведомление об ошибке.

            }
    }

//    /    MODERATOR - получить список рецептов на проверку - Функция загрузки
    fun loadPendingRecipes(
        page: Int = 0,
        size: Int = 10
    ) {
        viewModelScope.launch {
            _moderatorLoading.value = true
            _moderatorError.value = null

            try {
                Log.d("MODERATOR", "RecipeViewModel: _pendingRecipes.value: ${_pendingRecipes.value}")
                val response = repository.getPendingRecipes(page, size)
                Log.d("MODERATOR", "RecipeViewModel: response: ${response}")
                _pendingRecipes.value = response
            } catch (e: Exception) {
                _moderatorError.value = e.message
            } finally {
                _moderatorLoading.value = false
            }
        }
    }


    //    MODERATOR - обобрить
    fun approveRecipe(id: Long) {
        viewModelScope.launch {
            try {
                repository.approveRecipe(id)
                loadPendingRecipes()    //  перезагрузка
                refreshRecipe()
                myRecipesViewModel.refresh()
            } catch (e: Exception) {
                _moderatorError.value = e.message
            }
        }
    }

    //    MODERATOR - отклонить
    fun rejectRecipe(id: Long) {
        viewModelScope.launch {
            try {
                repository.rejectRecipe(id)
                loadPendingRecipes()
                refreshRecipe()
                myRecipesViewModel.refresh()
            } catch (e: Exception) {
                _moderatorError.value = e.message
            }
        }
    }

    fun checkIsModeratorDetail(value: Boolean) {
        _isModeratorDetail.value = value
        _isModeratorDetail.value = value
    }

//    +++++++++++++++++++++++++++++++++++++

}