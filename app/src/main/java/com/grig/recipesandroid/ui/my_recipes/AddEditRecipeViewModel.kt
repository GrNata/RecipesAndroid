package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.network.HttpException
import com.grig.recipesandroid.data.model.IngredientWithCaloriesAndAmount
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
import com.grig.recipesandroid.domain.model.IngredientErrorState
import com.grig.recipesandroid.ui.auth.AuthViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import com.grig.recipesandroid.ui.utilRecipe.UnitConvertor
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import kotlin.collections.any
import kotlin.collections.forEach
import kotlin.math.roundToInt

class AddEditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val unitRepository: UnitRepository,
    val authViewModel: AuthViewModel,
    private val recipesViewModel: RecipesViewModel,
    private val myRecipesViewModel: MyRecipesViewModel,
    private val navController: NavController
) : ViewModel() {

//    Флаг при загрузке формы создания (редактирования) - убрать удаление вводимых данных
    var isFormInitialized by mutableStateOf(false)

//    Id типа рецептов - Основное блюдо
    val TIP_BLYDA_ID = 1L

//    +++++++ состояние формы
    var name by mutableStateOf("")
    private set

    var description by mutableStateOf("")
        private set

    var image by mutableStateOf<String?> (null)
        private set

    var baseServings by mutableStateOf<Int?>(1)
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

//  Ошибка при выборе ингредиента
    val ingredientErrors = mutableStateListOf<IngredientErrorState>()


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

    fun loadCategoryTypes() {
        viewModelScope.launch {
            try {
                categoryTypesAll = categoryRepository.getCategoryTypes()
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
                Log.d("AddEdit-ingredient", "AddEditRecipeViewModel: ingredients: ${ingredients}")
                Log.d("AddEdit-ingredient", "AddEditRecipeViewModel: recipe.id: ${recipe.id} recipe.ingredients: ${recipe.ingredients}")

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
    Log.d("ADD RECIPE-createRecipe", "AddEditRecipeViewModel: START")
        viewModelScope.launch {
            try {
                isLoading = true

                Log.d("ADD RECIPE-createRecipe", "AddEditRecipeViewModel: createRecipe, name: ${name}, desc: $description" +
                        ", selectedCategoryValues:${selectedCategoryValues}, ing size: ${ingredients.size}, step size: ${steps.size}")

                val categoryIds = selectedCategoryValues.values.map { it.id }
                if (categoryIds.isEmpty()) throw IllegalArgumentException("Должна быть выбрана хотя бы одна категория")

                val response = recipeRepository.createRecipe(
                    RecipeCreateRequest(
                        name = name,
                        description = description,
                        image = image,
                        baseServings = baseServings,
//                        categoryIds = categoriesAll.map { it.id },
                        categoryValueIds = categoryIds,
                        ingredients = ingredients.toList(),
                        steps = steps.toList()
                    )
                )
                Log.d("ADD RECIPE-createRecipe", "AddEditRecipeViewModel: response: ${response}")

                onSuccess()

            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("ADD RECIPE-createRecipe", "AddEditRecipeViewModel: Error-1: ${e.message}")
            } catch (e: HttpException) {
                errorMessage = "Ошибка создания рецепта (${e.message}"
                Log.e("ADD RECIPE-createRecipe", "AddEditRecipeViewModel: Error-2: ${e.message}")
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
                        baseServings = baseServings,
                        categoryIds = categoryIds,
                        ingredients = ingredients.toList(),
                        steps = steps.toList()
                    )
                )
                onRecipeUpdate(recipeId)
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
//    перенесла в RecipeViewModel

//    val recipes = recipesViewModel.recipesPagingFlow
//    fun deleteRecipe(recipeId: Long) {
//    Log.d("ADD RECIPE-newEdit", "AddEditRecipeViewModel: deleteRecipe, START")
////        val recipeId = currentRecipeId ?: return
//    Log.d("ADD RECIPE-newEdit", "AddEditRecipeViewModel: deleteRecipe, recipeID=$recipeId")
//        viewModelScope.launch {
//            try {
//                recipeRepository.deleteRecipe(recipeId)
////                onRecipeSave()
//
//            } catch (e: Exception) {
//                errorMessage = e.message
//            }
//        }
//    }

    fun onRecipeSave(
        isEdit: Boolean,
        onSuccess: () -> Unit
    ) {
        Log.d("ADD RECIPE-onRecipeSave", "AddEditRecipeViewModel: isEdit=$isEdit")
        if (!validateRecipe()) {
            Log.d("ADD RECIPE-onRecipeSave", "AddEditRecipeViewModel: - не прошла валидация!!!")
            return  // Если не прошла валидация, показываем Snackbar с errorMessage
        }

        if (isEdit) {
            updateRecipe(onSuccess)
        } else {
            Log.d("ADD RECIPE-onRecipeSave", "AddEditRecipeViewModel: BEFORE CREATE")
            createRecipe(onSuccess)
        }
    }

    fun validateRecipe(): Boolean {
        var isValid = true

//        1. Название
        if (name.isBlank()) {
            errorMessage = "Название рецепта обязательно!"
            isValid = false
        }
//        2. Категория
        if (!selectedCategoryValues.containsKey(TIP_BLYDA_ID)) {
            errorMessage = "Выберите хотя бы одну категорию. \n Категория \"Тип блюда\" обязательна!"
            isValid = false
        }
//        ОБЯЗАТЕЛЬНО - категория (id = 1) - Тип блюда
        val hasRequiredCategory = selectedCategoryValues.values.any { it.id == 1L }
        if (!hasRequiredCategory) {
            errorMessage = "Обязательная категория - «Тип блюда»"
            return false
        }
//        3. Ингредиенты
        if (ingredients.isEmpty()) {
            errorMessage = "Ингредиенты обязательны!"
            isValid = false
        } else {
//            Валидация каждого ингредиента
            ingredients.indices.forEach { validateIngredient(it) }

            if (ingredients.indices.any { index ->
                    ingredientErrors[index].amountError ||
                            ingredientErrors[index].unitError ||
                            ingredients[index].ingredientId == 0L
                }) {
                errorMessage = "Заполните все обязательные поля ингредиентов!"
                isValid = false
            }
        }
//        4. Шаги приготовления (можно минимум 1 шаг)
        if (steps.isEmpty()) {
            errorMessage = "Добавте хотя бы один шаг приготовления."
            isValid = false
        }
//        5. Количество порций по умолчанию одна
        if (baseServings == null || requireNotNull(baseServings) <= 0) {
            baseServings = 1
            errorMessage = "Количество порций по умолчанию будет одна."
        }
        return isValid
    }

    fun onRecipeUpdate(recipeId: Long) {
        recipesViewModel.refreshRecipe()
        myRecipesViewModel.refresh()
        navController.navigate("my_recipes") {
            popUpTo("recipe_edit/${recipeId}") { inclusive = true }
        }
//        navController.popBackStack()
    }

//    ++++++++++++++
//    Setters (сеттеры) для UI
//    ++++++++++++++
    fun onNameChange(value: String) {
        name = value
//        Log.d("ADD RECIPE-newEdit", "AddEditRecipeScreenViewModel onNameChange: value=$value, name=$name")
    }
    fun onDescriptionChange(value: String) { description = value }
    fun onImageChange(value: String?) { image = value }

//    Количество порций
    fun onBaseServings(value: String) {
        baseServings = value.toIntOrNull()
    }


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
        ingredientErrors.add(IngredientErrorState(amountError = true, unitError = true))
    }

    fun removeIngredient(index: Int) {
        if (index in ingredients.indices) {
            ingredients.removeAt(index)
            ingredientErrors.removeAt(index)
        }
    }

    fun onIngredientSelected(index: Int, ingredient: IngredientDto) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(ingredient.id)
    }

    fun onIngredientAmountChange(index: Int, amount: String) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(amount = amount)
    }

    fun onUnitSelected(index: Int, unit: UnitDto) {
        if (index in ingredients.indices) ingredients[index] = ingredients[index].copy(unitId = unit.id)
//        ingredients[index] = ingredients[index].copy(
//            unitId = unit.id
//        )
    }

    fun cleanEmptyIngredients() {
        ingredients.removeAll { it.ingredientId == 0L && (it.amount?.isBlank() ?: true) && it.unitId == null }
    }

//    Валидация - подсветка если amount и unit не заполнены
    fun validateIngredient(index: Int) {
        val ing = ingredients.getOrNull(index) ?: return

        // Если список ошибок короче, добавляем пустые элементы
        while (ingredientErrors.size <= index) {
            ingredientErrors.add(IngredientErrorState(false, false))
        }

        ingredientErrors[index] = IngredientErrorState(
                amountError = ing.amount.isNullOrBlank() || ing.amount.toDoubleOrNull() == null,
                unitError = ing.unitId == null
            )
    }

//    Общая проверка перед сохранением
    fun validateAll() : Boolean {
        ingredients.indices.forEach { validateIngredient(it) }
        return ingredientErrors.none { it.amountError || it.unitError }
    }

    fun clearError() {
        errorMessage = null
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
    Log.d("ADD RECIPE-newEdit", "AddEditRecipeViewModel: resetForm, name: ${name}")
        currentRecipeId = null
        name = ""
        description = ""
        image = null

        selectedCategoryValues.clear()
        ingredients.clear()
        steps.clear()
        errorMessage = null

//        isFormInitialized = false
    }

//    ++++++++++++++++++
//    Калории

    var totalCalories by mutableStateOf(0)
        private set
    private val unitConvertor: UnitConvertor = UnitConvertor()
    fun calculationCalories() {
        val ingredientWithCalories = ingredients.mapNotNull { ing ->
            val ingredient = ingredientsAll.firstOrNull { it.id == ing.ingredientId } ?: return@mapNotNull null
            val unitDto = unitsAll.firstOrNull { it.id == ing.unitId } ?: return@mapNotNull null
            val amountDouble = ing.amount?.toDoubleOrNull() ?: return@mapNotNull null

            val gram = unitConvertor.toGram(amountDouble, unitDto, ingredient.name)
                ?: return@mapNotNull null

            IngredientWithCaloriesAndAmount(
                id = ingredient.id,
                name = ingredient.name,
                energyKcal100g = ingredient.energyKcal100g,
                amount = gram,
                unitCode = "G"
            )
        }
        val totalCalor = ingredientWithCalories.sumOf { item ->
            // Берем калории (если null, то 0) и умножаем на количество, деленное на 100
            val energy = item.energyKcal100g?.toDouble() ?: 0.0
            val am = item.amount ?: 0.0
            (energy * (am / 100.0)).also { result ->
                Log.d(
                    "Calories",
                    "AddEditRecipeViewModel: energy: ${energy}, am: $am, energy: ${result}"
                )
            }
        }
        totalCalories = totalCalor.roundToInt()
    }

    fun getIngredientCalories(index: Int): Int? {
        val ing = ingredients.getOrNull(index) ?: return null
        val ingredient = ingredientsAll.firstOrNull { it.id == ing.ingredientId } ?: return null
        val unit = unitsAll.firstOrNull { it.id == ing.unitId } ?: return null
        val amaunt = ing.amount?.toDoubleOrNull() ?: return null

        val grams = unitConvertor.toGram(amaunt, unit, ingredient.name) ?: return null
        val kcal100 = ingredient.energyKcal100g ?: return null

        return ((kcal100 * grams) / 100).roundToInt()
    }
}