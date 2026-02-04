package com.grig.recipesandroid.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.grig.recipesandroid.data.model.dto.CategoryTypeCreate
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto
import com.grig.recipesandroid.data.model.dto.CategoryTypeUpdate
import com.grig.recipesandroid.data.model.dto.CategoryValueCreate
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.CategoryValueUpdate
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.FavoritesRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.domain.model.Recipe
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
import kotlinx.coroutines.flow.combine
import kotlin.collections.emptyList

//  ViewModel отвечает за данные (Flow<PagingData>) и их загрузку из репозитория
open class RecipesViewModel(
    private val repository: RecipeRepository,
    private val favoritesRepository: FavoritesRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val userIdFlow: StateFlow<String?>      // сюда передаем текущий userId / email
) : ViewModel() {

//    Любое изменение данных → invalidate()
    private val refreshTrigger = MutableStateFlow(0)

    fun refreshRecipe() {
        refreshTrigger.update { it + 1 }
//        refreshTrigger.tryEmit(Unit)
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

////    Теперь добавим объект Pager с Retry. Paging уже умеет повторять загрузку через метод retry() у PagingData. Нам нужно просто сохранить Flow, чтобы в UI можно было вызвать повтор
//    lateinit var lastRecipesPagingFlow: PagingData<RecipeDto>

//    Поиск
//🔹 Никаких launch, loadRecipes, StateFlow
//🔹 Paging сам управляет загрузкой
// Flow с debounce и фильтрацией в PagingSource
//    val recipesPagingFlow: Flow<PagingData<RecipeDto>> =
    val recipesPagingFlow: Flow<PagingData<Recipe>> =
        combine(
            refreshTrigger,
            _query.debounce(300).distinctUntilChanged()
        ) { _, query -> query }
            .flatMapLatest { query ->
                repository.getRecipesPaper(query = query)
                    .catch { e ->
                        _messageFlow.emit("Ошибка загрузки рецептов: ${e.localizedMessage}")
                    }
            }
            .cachedIn(viewModelScope)

    private var favoritesSyncedForUser: String? = null

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

//            ingredientsDictionary = ingredientRepository.getAllIngredients()
//            Log.d("SEARCH INGREDIENT", "RecipesViewModel: init ingredients: ${ingredientsDictionary}")
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

//    Удаление рецепта
    fun deleteRecipe(recipeId: Long) {
        Log.d("ADD RECIPE-newEdit", "AddEditRecipeViewModel: deleteRecipe, START")
//        val recipeId = currentRecipeId ?: return
        Log.d("ADD RECIPE-newEdit", "AddEditRecipeViewModel: deleteRecipe, recipeID=$recipeId")
        viewModelScope.launch {
            try {
                repository.deleteRecipe(recipeId)

                refreshRecipe()

            } catch (e: Exception) {
                errorMessage = e.message
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

    fun createCategoryType(categoryType: CategoryTypeCreate) {
        viewModelScope.launch {
            categoryRepository.createCategoryType(categoryType)
            refreshCategoryType()
        }
    }

    fun updateCategoryType(id: Long, categoryType: CategoryTypeUpdate) {
        viewModelScope.launch {
            categoryRepository.updateCategoryType(id, categoryType)
            refreshCategoryType()
        }
    }

    fun deleteCategoryType(id: Long) {
        viewModelScope.launch {
            categoryRepository.deleteCategoryType(id)
            refreshCategoryType()
        }
    }
//    _______________________
    fun refreshCategoryValues() {
        viewModelScope.launch {
            categoryValuesAll = categoryRepository.getCategoryValues()
        }
    }
//
//    fun createCategoryValue(categoryValue: CategoryValueCreate) {
//        viewModelScope.launch {
//            categoryRepository.createCategoryValues(categoryValue)
//            refreshCategoryValues()
//        }
//    }
//
//    fun updateCategoryValue(id: Long, categoryValue: CategoryValueUpdate) {
//        viewModelScope.launch {
//            categoryRepository.updateCategoryValue(id, categoryValue)
//            refreshCategoryValues()
//        }
//    }
//
//    fun deleteCategoryValue(id: Long) {
//        viewModelScope.launch {
//            categoryRepository.deleteCategoryValue(id)
//            refreshCategoryValues()
//        }
//    }
//    ______________________

    fun refreshIngredients() {
        viewModelScope.launch {
            ingredientsDictionary = ingredientRepository.getAllIngredients()
        }
    }

//    fun createIngredient(ingredient: IngredientCreate) {
//        viewModelScope.launch {
//            ingredientRepository.createIngredient(ingredient)
//            refreshIngredients()
//        }
//    }
//
//    fun updateIngredient(id: Long, ingredient: IngredientUpdate) {
//        viewModelScope.launch {
//            ingredientRepository.updateIngredient(id, ingredient)
//            refreshIngredients()
//        }
//    }
//
//    fun deleteIngredient(id: Long) {
//        viewModelScope.launch {
//            ingredientRepository.deleteIngredient(id)
//            refreshIngredients()
//        }
//    }
//    +++++++++++++++++++++++++++++++++++++

}