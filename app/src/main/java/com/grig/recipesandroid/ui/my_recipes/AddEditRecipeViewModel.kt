package com.grig.recipesandroid.ui.my_recipes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grig.recipesandroid.data.model.dto_request.IngredientRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeCreateRequest
import com.grig.recipesandroid.data.model.dto_request.RecipeUpdateRequest
import com.grig.recipesandroid.data.repository.RecipeRepository
import com.grig.recipesandroid.domain.model.Category
import com.grig.recipesandroid.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

class AddEditRecipeViewModel(
    private val recipeRepository: RecipeRepository,
    val authViewModel: AuthViewModel
) : ViewModel() {

//    состояние формы
    var name by mutableStateOf("")
    private set

    var description by mutableStateOf("")
        private set

    var image by mutableStateOf<String?> (null)
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

//    var ingredients by mutableStateOf<List<RecipeIngredient>>(emptyList())
//    var ingredients: List<RecipeIngredient> by mutableStateOf<List<Ingredient>>(emptyList())
//    var ingredients by mutableStateOf<List<Ingredient>>(emptyList())
    var ingredients by mutableStateOf<List<IngredientRequest>>(emptyList())
        private set

    var steps by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var currentRecipeId: Long? = null

//     ++++++++++++++++++++
//    Load (EDIT)
//    +++++++++++++++++++++
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
                categories = recipe.categories
                ingredients = recipe.ingredients.map { ing ->
                    IngredientRequest(
                        ingredientId = ing.ingredient.id,
                        amount = ing.amount.orEmpty(),
                        unitId = ing.unit?.id
                    )
                }
                steps = recipe.steps
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
                        categoryIds = categories.map { it.id },
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
                        categoryIds = categories.map { it.id },
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

}