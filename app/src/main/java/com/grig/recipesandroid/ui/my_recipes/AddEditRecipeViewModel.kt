package com.grig.recipesandroid.ui.my_recipes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.dto_request.IngredientDto
import com.grig.recipesandroid.data.model.dto_request.IngredientRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeUpdateRequest
import com.grig.recipesandroid.data.model.dto_request.UnitDto
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.data.repository.UnitRepository
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import java.util.Collections.emptyList

class AddEditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val unitRepository: UnitRepository,
    val authViewModel: AuthViewModel
) : ViewModel() {

//    состояние формы
    var name by mutableStateOf("")
    private set

    var description by mutableStateOf("")
        private set

    var image by mutableStateOf<String?> (null)
        private set

    var categoriesAll by mutableStateOf<List<Category>>(emptyList())
        private set
//  выбранные ингредиенты рецепта
//    var ingredients by mutableStateOf<List<IngredientRequest>>(emptyList())
    var ingredients = mutableStateListOf<IngredientRequest>()
        private set

//    var steps by mutableStateOf<List<String>>(emptyList())
    var steps = mutableStateListOf<String>()
        private set
//    private var _steps = mutableListOf<String>()
//    val steps: List<String> get() = _steps

//    справочник
    var unitsAll by mutableStateOf<List<UnitDto>>(emptyList())
        private set
    //    справочник
    var ingredientsAll by mutableStateOf<List<IngredientDto>>(emptyList())


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var currentRecipeId: Long? = null

    var selectedCategory by mutableStateOf<Category?>(null)

//     ++++++++++++++++++++
//    Load (EDIT)
//    +++++++++++++++++++++

//    Отдельная загрузка справочников (ОДИН РАЗ)
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
                categoriesAll = recipe.categories

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
//                ingredients = recipe.ingredients.map { ing ->
//                    IngredientRequest(
////                    IngredientDto(
//                        ingredientId = ing.ingredient.id,
//                        amount = ing.amount.orEmpty(),
//                        unitId = ing.unit?.id
//                    )
//                }
//                ingredientsAll = recipe.ingredients.map { ing ->
//                    IngredientDto(
//                        id = ing.ingredient.id,
//                        name = ing.ingredient.name
//                    )
//                }
//                unitsAll = recipe.ingredients.map { ing ->
//                    UnitDto(
//                        id = ing.unit?.id ?: 0L,
//                        code = ing.unit?.code ?: "",
//                        label = ing.unit?.label ?: ""
//                    )
//                }
//                steps = recipe.steps
                steps.clear()
                steps.addAll(recipe.steps)

                selectedCategory = recipe.categories.firstOrNull()?.let { recCat ->
                    categoriesAll.firstOrNull { it.id == recCat.id}
                }

                Log.d("GET-INGRED", "AddEditRecipeViewModel ingredients size: ${ingredients.size}")

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
    fun createRecipe() {
        viewModelScope.launch {
            try {
                isLoading = true

                recipeRepository.createRecipe(
                    RecipeCreateRequest(
                        name = name,
                        description = description,
                        image = image,
                        categoryIds = categoriesAll.map { it.id },
                        ingredients = ingredients,
                        steps = steps
                    )
                )
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
    fun updateRecipe() {
        val recipeId = currentRecipeId ?: return
        viewModelScope.launch {
            try {
                isLoading = true
                recipeRepository.updateRecipe(
                    recipeId = recipeId,
                    request = RecipeUpdateRequest(
                        name = name,
                        description = description,
                        image = image,
                        categoryIds = categoriesAll.map { it.id },
                        ingredients = ingredients,
                        steps = steps
                    )
                )
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
//    setters для UI
//    ++++++++++++++
    fun onNameChange(value: String) { name = value}
    fun onDescriptionChange(value: String) { description = value }
    fun onImageChange(value: String?) { image = value }


//    ++++++++++++
//    для запоминания категории (Редактирование)
//    ++++++++++++
    fun onCategorySelected(category: Category) {
        selectedCategory = category
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                categoriesAll = categoryRepository.getCategories()
                Log.d("GET-CATEGORIES", "AddEditRecipeViewModel categoriesAll size = ${categoriesAll.size}")
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("GET-CATEGORIES", "Ошибка загрузки категорий", e)
            }
        }
    }

//    +++++++++++++
//    INGREDIENT
//    ++++++++++++
    fun addIngredient() {
//        ingredients = ingredients + IngredientRequest(ingredientId = 0L, amount = "", unitId = null)
        ingredients.add(IngredientRequest(0L, "", null))
    }

    fun removeIngredient(index: Int) {
//        ingredients = ingredients.toMutableList().apply { removeAt(index) }
        ingredients.removeAt(index)
    }

    fun onIngredientSelected(index: Int, ingredient: IngredientDto) {
        if (index !in ingredients.indices) return
        ingredients[index] = ingredients[index].copy(ingredient.id)
//        ingredients = ingredients.toMutableList().apply {
//            this[index] = this[index].copy(ingredientId =  ingredient.id)
//        }
    }

    fun onIngredientAmountChange(index: Int, amount: String) {
        if (index !in ingredients.indices) return
        ingredients[index] = ingredients[index].copy(amount = amount)
//        ingredients = ingredients.toMutableList().apply {
//            this[index] = this[index].copy(amount = amount)
//        }
    }

    fun onUnitSelected(index: Int, unit: UnitDto) {
        if (index !in ingredients.indices) return
        ingredients[index] = ingredients[index].copy(unitId = unit.id)
//        ingredients = ingredients.toMutableList().apply {
//            this[index] = this[index].copy(unitId = unit.id)
//        }
    }

    fun addStep(index: Int, newStep: String) {
//        _steps.add(index, newStep)
//        steps = steps + newStep
        steps.add(index, newStep)
    }

    fun updateStep(index: Int, newText: String) {
//        if (index in _steps.indices) {
//            _steps[index] = newText
//        }
        if (index in steps.indices) {   // проверка границ
            steps[index] = newText
//            steps = steps.toMutableList().apply {
//                this[index] = newText   // заменяем текст шага
//            }
        }
    }

    fun removeStep(index: Int) {
        if (index in steps.indices) {
            steps.removeAt(index)
//            steps = steps.toMutableList().apply { removeAt(index) }
//            _steps.removeAt(index)
        }
    }

//    // Для отображения текста в TextField
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
    fun resetForm() {
        currentRecipeId = null
        name = ""
        description = ""
        image = null

        selectedCategory = null
        categoriesAll = categoriesAll

        ingredients.clear()
//        ingredients = emptyList()
//        ingredientsAll = emptyList()
//        unitsAll = emptyList()

        steps.clear()
        errorMessage = null
    }

}