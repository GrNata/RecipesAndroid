package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.data.model.request.IngredientRequest
import com.grig.recipesandroid.data.model.request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.dto.UnitDto
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import kotlin.collections.forEach

class AddEditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val unitRepository: UnitRepository,
    val authViewModel: AuthViewModel
) : ViewModel() {

//    Id типа рецептов - Основное блюдо
    val TIP_BLYDA_ID = 1L

//    +++++++ состояние формы
    var name by mutableStateOf("")
    private set

    var description by mutableStateOf("")
        private set

    var image by mutableStateOf<String?> (null)
        private set
//    ++++++++++++

    // +++++++++ Категории
    // Состояние выбранных категорий - списка рецептов
    val selectedCategoryValues = mutableStateMapOf<Long, CategoryValueDto>()
//    val selectedCategoryValues = mutableMapOf<Long, CategoryValueDto>()
    // Состояние выбранных категорий - при создании и редактировании рецепта
    val selectedCategoryValuesForAddUpdate = mutableMapOf<Long, CategoryValueDto>()
    var categoryValuesAll by mutableStateOf<List<CategoryValueDto>>(emptyList())
        private set

//    +++++++++++++++

// +++++++++ выбранные ингредиенты рецепта и шаги
    var ingredients = mutableStateListOf<IngredientRequest>()
        private set

    var steps = mutableStateListOf<String>()
        private set

//  ++  справочник единиц измерения
    var unitsAll by mutableStateOf<List<UnitDto>>(emptyList())
        private set
    // ++   справочник ингредиентов
    var ingredientsAll by mutableStateOf<List<IngredientDto>>(emptyList())
//    +++  справочник типов категорий
    var categoryTypesAll by mutableStateOf<List<CategoryTypeDto>>(emptyList())
//+++++++++++++++++++++++++

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var currentRecipeId: Long? = null

//    var selectedCategory by mutableStateOf<Category?>(null)


//     ++++++++++++++++++++
//    Load (EDIT)
//    +++++++++++++++++++++

// +++++++   Отдельная загрузка справочников (ОДИН РАЗ)
    fun loadIngredientAndUnitDictionaries() {
        viewModelScope.launch {
            try {
                ingredientsAll = ingredientRepository.getAllIngredients()
                unitsAll = unitRepository.getAllUnits()
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

    // загружает все CategoryValueDto
    fun loadCategoryValues() {
        viewModelScope.launch {
            try {
                categoryValuesAll = categoryRepository.getCategoryValues()  // получаем все CategoryValueDto
                Log.d("CATEGORY-ch", "AddEditRecipeViewModel categoryValuesAll size = ${categoryValuesAll.size}")
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("CATEGORY-ch", "AddEditRecipeViewModel: Ошибка загрузки категорий: ${e}")
            }
        }
    }
//    +++++++++++++++++++++++
// ===== Загрузка рецепта для редактирования =====
    fun loadRecipe(recipeId: Long) {
        if (currentRecipeId == recipeId) return
        currentRecipeId = recipeId
        isLoading = true

        viewModelScope.launch {
            try {
                var recipe = recipeRepository.getRecipesById(recipeId)

                name = recipe.name
                description = recipe.description.orEmpty()
                image = recipe.image


                // ВСЕ возможные категории (для выпадающих списков)
                loadCategoryValues() // вызываем метод загрузки справочника категорий
                // Если recipe.categoryValueIds приходит с сервера, они уже CategoryValueDto
                // В categoryValuesAll оставляем весь справочник, а selectedCategoryValues заполняем выбранное
                selectedCategoryValues.clear()
                recipe.categories.forEach { cv ->
                    selectedCategoryValues[cv.categoryTypeId] = CategoryValueDto(
                        id = cv.id,
                        typeId = cv.categoryTypeId,
                        typeName = cv.categoryTypeName,
                        categoryValue = cv.categoryValue
                    )
                }
                categoryTypesAll = categoryRepository.getCategoryTypes()
                Log.d("CATEGORY-ch", "AddEditRecipeViewModel: loaded ${categoryTypesAll.size} category type")


                ingredients.clear()
                ingredients.addAll(
                    recipe.ingredients.map {
                        IngredientRequest(
                            ingredientId = it.ingredient.id,
                            amount = it.amount.orEmpty(),
                            unitId = it.unit?.id
                        )
                    }
                )
                steps.clear()
                steps.addAll(recipe.steps)

                // заполняем selectedCategoryValues при редактировании
                selectedCategoryValues.clear()
                recipe.categories.forEach { cv ->
                    selectedCategoryValues[cv.categoryTypeId] = CategoryValueDto(
                        id = cv.id,
                        typeId = cv.categoryTypeId,
                        typeName = cv.categoryTypeName,
                        categoryValue = cv.categoryValue
                    )
                }

                Log.d("CATEGORY-ch", "AddEditRecipeViewModel: ingredients size: ${ingredients.size}")

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

//    +++++++++++++
//    CREATE
//    ++++++++++++
// ===== Создание рецепта =====
    fun createRecipe(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true

                Log.d("ADD RECIPE", "AddEditRecipeViewModel: START, name: ${name}, desc: $description" +
                        ", cat.size:${categoryValuesAll.size}, ing size: ${ingredients.size}, step size: ${steps.size}")

                val categoryIds = selectedCategoryValues.values.map { it.id }
                if (categoryIds.isEmpty()) throw IllegalArgumentException("Должна быть выбрана хотя бы одна категория")

                recipeRepository.createRecipe(
                    RecipeCreateRequest(
                        name = name,
                        description = description,
                        image = image,
//                        categoryIds = categoriesAll.map { it.id },
                        categoryValueIds = categoryIds,
                        ingredients = ingredients.toList(),
                        steps = steps.toList()
                    )
                )
                onSuccess()

            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

//    +++++++++++++
//    UPDATE
//    +++++++++++++
// ===== Обновление рецепта =====
    fun updateRecipe(onSuccess: () -> Unit) {
        val recipeId = currentRecipeId ?: return
        viewModelScope.launch {
            try {
                isLoading = true

                val categoryIds = selectedCategoryValues.values.map { it.id }

                recipeRepository.updateRecipe(
                    recipeId = recipeId,
                    request = RecipeUpdateRequest(
                        name = name,
                        description = description,
                        image = image,
                        categoryIds = categoryIds,
//                        categoryIds = categoryValuesAll.map { it.id },
                        ingredients = ingredients.toList(),
                        steps = steps.toList()
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

//    ++++++++++++
//    DELETE
//    ++++++++++++
// ===== Удаление рецепта =====
    fun deleteRecipe() {
        val recipeId = currentRecipeId ?: return
        viewModelScope.launch {
            try {
                recipeRepository.deleteRecipe(recipeId)
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }

//    ++++++++++++++
//    Setters (сеттеры) для UI
//    ++++++++++++++
    fun onNameChange(value: String) { name = value}
    fun onDescriptionChange(value: String) { description = value }
    fun onImageChange(value: String?) { image = value }


//    ++++++++++++
//    КАТЕГОРИИ
//    ++++++++++++
    //    Метод toggle для выбора категории по типу в списке рецептов
    fun toggleCategoryValue(categoryValue: CategoryValueDto) {
        // сохраняем только по типу
        selectedCategoryValues[categoryValue.typeId] = categoryValue
    }

    fun getCategoryValuesForType(id: Long): List<CategoryValueDto> =
        categoryValuesAll
            .filter { it.typeId == id }

//  Для создания и редактирования рецепта
    fun toggleCategoryValueAddEdit(categoryValue: CategoryValueDto) {
//        selectedCategoryValuesForAddUpdate[categoryValue.typeId] = categoryValue
        selectedCategoryValues[categoryValue.typeId] = categoryValue
    }

    // Проверка при сохранении
    fun validateCategoriesForCreateRecipe() : Boolean {
//        val temp = selectedCategoryValuesForAddUpdate.containsKey(TIP_BLYDA_ID)    // TIP_BLYDA_ID = 4 (Основное блюдо)
        val temp = selectedCategoryValues.containsKey(TIP_BLYDA_ID)    // TIP_BLYDA_ID = 4 (Основное блюдо)
//        Log.d("AddEdit-category", "AddEditViewModul: temp: $temp,  selectedCategoryValuesForAddUpdate: $selectedCategoryValuesForAddUpdate")
        Log.d("AddEdit-category", "AddEditViewModul: temp: $temp,  selectedCategoryValues: $selectedCategoryValues")
        return temp
    }

//++++++++++++++++++++

//    +++++++++++++
//    INGREDIENT
//    ++++++++++++
    fun addIngredient() {
        ingredients.add(IngredientRequest(0L, "", null))
    }

    fun removeIngredient(index: Int) {
        if (index in ingredients.indices) ingredients.removeAt(index)
    }

    fun onIngredientSelected(index: Int, ingredient: IngredientDto) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(ingredient.id)
    }

    fun onIngredientAmountChange(index: Int, amount: String) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(amount = amount)
    }

    fun onUnitSelected(index: Int, unit: UnitDto) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(unitId = unit.id)
    }

    // ===== Шаги =====
    fun addStep(index: Int, newStep: String) {
        steps.add(index, newStep)
    }

    fun updateStep(index: Int, newText: String) {
        if (index in steps.indices) {   // проверка границ
            steps[index] = newText
        }
    }

    fun removeStep(index: Int) {
        if (index in steps.indices) {
            steps.removeAt(index)
        }
    }

//    // Для отображения текста в TextField
// ===== Получение текста для UI =====
    fun getIngredientName(index: Int): String {
        val ingId = ingredients.getOrNull(index)?.ingredientId ?: return ""
        return ingredientsAll.find { it.id== ingId }?.name ?: ""
    }

    fun getUnitName(index: Int): String {
        val unitId = ingredients.getOrNull(index)?.unitId ?: return ""
        return unitsAll.find { it.id == unitId }?.label ?: ""
    }


//    ++++++++++++
//    Сбрасывание значение полей
// ===== Сброс формы =====
    fun resetForm() {
        currentRecipeId = null
        name = ""
        description = ""
        image = null

        selectedCategoryValues.clear()
//        selectedCategoryValuesForAddUpdate.clear()
//        selectedCategory = null
//        categoryValuesAll = categoryValuesAll

        ingredients.clear()
        steps.clear()
        errorMessage = null
    }

}