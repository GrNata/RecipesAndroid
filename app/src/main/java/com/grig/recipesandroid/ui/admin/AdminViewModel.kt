package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import com.grig.recipesandroid.data.model.dto.CategoryValueCreate
import com.grig.recipesandroid.data.model.dto.CategoryValueUpdate
import com.grig.recipesandroid.data.model.dto.IngredientAddEdit
import com.grig.recipesandroid.data.model.dto.IngredientRequest
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.data.repository.CategoryRepository
import com.grig.recipesandroid.data.repository.IngredientRepository
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class AdminViewModel(
    private val authRepository: AuthRepository,
    private val ingredientRepository: IngredientRepository,
    private val categoryRepository: CategoryRepository,
    private val recipesViewModel: RecipesViewModel,
    private val navController: NavController
) : ViewModel() {

    private val _usersAll = MutableStateFlow<List<UserRequest>>(emptyList())
    val usersAll: StateFlow<List<UserRequest>> = _usersAll

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var _error = MutableStateFlow<String?>(null)
    var error: StateFlow<String?> = _error

    //    +++++++ состояние формы
//    var ingredientId by mutableStateOf(null)
//        private set

    var name by mutableStateOf("")
        private set

    var nameEng by mutableStateOf<String?>("")
        private set

    var energyKcal100g by mutableStateOf<Int?>(0)
        private set

    private var currentIngredientId: Long? = null

    var nameCategoryValue by mutableStateOf<String>("")
        private set

    var nameCategoryTypeByValue by mutableStateOf<String>("")
        private set

    var typeIdCategoryValue by mutableStateOf<Long?>(1L)
        private set

    var nameCategoryType by mutableStateOf<String>("")
        private set

    private var currentCategoryValueId: Long? = null

    private var currentCategoryTypeId: Long? = null


    fun onNameChange(value: String) {
        name = value
        Log.d("ADMIN", "AdminScreenViewModel onNameChange: value=$value, name=$name")
    }

    fun onNameEngChange(value: String) {
        nameEng = value
    }

    fun onEnergyKcal100Change(value: String) {
        energyKcal100g = value.toIntOrNull()
    }

    fun onNameCategoryValueChange(value: String) {
        nameCategoryValue = value
    }

    fun onNameCategoryTypeByValueChange(value: String) {
        nameCategoryTypeByValue = value
    }

    fun onTypeIdCategoryValueChange(value: String) {
        typeIdCategoryValue = value.toLongOrNull()
    }

    fun onNameCategoryTypeChange(value: String) {
        nameCategoryType = value
    }

    fun resetFormIngredient() {
            Log.d("ADMIN", "AddminViewModel: resetForm, name: ${name}")
            currentIngredientId = null
            name = ""
            nameEng = ""
            energyKcal100g = 0

            _error.value = null
    }

    fun resetFormCategoryValue() {
        nameCategoryValue = ""
        nameCategoryTypeByValue = ""
        typeIdCategoryValue = null
        _error.value = null
    }

    fun resetFormCategoryType() {
        nameCategoryType = ""
        _error.value = null
    }


    fun loadUsers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _usersAll.value = authRepository.getAllUsers()
                Log.d("ADMIN", "AdminViewModel: usersAll: ${_usersAll.value}")

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

//    fun updateRole(userId: Long, newRole: UpdateUserRoleRequest) {
    fun updateRole(userId: Long, newRole: UpdateUserRoleResponse) {
        Log.d("ADMIN", "AdminViewModel: newRole: ${newRole}")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                authRepository.updateRoleUser(userId, newRole)
                // Обновляем список пользователей
                loadUsers()
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    suspend fun updateBlockedUser(userId: Long, blocked: BlockUserRequest) {
        _loading.value = true
        _error.value = null
        try {
            authRepository.updateBlockedUser(userId, blocked)
            loadUsers()
        } catch (e: Exception) {
            _error.value = e.message
            Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
        } finally {
            _loading.value = false
        }
    }

//    +++++++++++++++++++

    fun isValid(): Boolean {
        return name.isNotBlank() && (energyKcal100g == null )
//        return name.isNotBlank() && (energyKcal100g == null || energyKcal100g >= 0  )
    }

    fun getIngredientAddEdit(ingredientId: Long?) : IngredientAddEdit? {
        if (isValid()) return null
        return IngredientAddEdit(
            id = ingredientId,
            name = name,
            nameEng = nameEng,
            energyKcal100g = energyKcal100g
        )
    }


    fun onUpdateIngredients() {
        recipesViewModel.refreshIngredients()
        navController.navigate("admin_ingredient")
//        {
//            popUpTo("recipe_edit/${recipeId}") { inclusive = true }
//        }
    }
    fun loadIngredientById(id: Long?) {
        Log.d("ADMIN", "loadIngredientById: id=$id")

        if (id == null) {
            _error.value = "ID не указан"
            return
        }
        if (currentIngredientId == id) return
        currentIngredientId = id
        _loading.value = true

        viewModelScope.launch {
            try {
                Log.d("ADMIN", "Запрос к API с id=$id")
                val ingredient = ingredientRepository.getIngredientById(id)

                Log.d("ADMIN", "AdminViewModel: ingredient: ${ingredient}")

                name = ingredient.name
                nameEng = ingredient.nameEng
                energyKcal100g = ingredient.energyKcal100g

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

//    fun createIngredient(ingredient: IngredientCreate) {
    fun saveIngredient(isEdit: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true

                if (isEdit) {
                    Log.d("ADMIN", "AdminViewModel: update ingredient id = $currentIngredientId")
                    val ingredientId = currentIngredientId ?: return@launch

//                    ingredientRepository.updateIngredient(ingredientId, IngredientUpdate(
                    ingredientRepository.updateIngredient(ingredientId, IngredientRequest(
//                        id = currentIngredientId,
                        name = name,
                        nameEng = nameEng,
                        energyKcal100g = energyKcal100g
                    ))
                    onUpdateIngredients()
//                    recipesViewModel.refreshIngredients()
                } else {
                    ingredientRepository.createIngredient(IngredientRequest(
                        name = name,
                        nameEng = nameEng,
                        energyKcal100g = energyKcal100g
                    ))
                    recipesViewModel.refreshIngredients()
                }
                onSuccess()

            } catch (e: kotlin.Exception) {
                _error.value = e.message
            } catch (e: HttpException) {
                _error.value = e.message()
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteIngredient(id: Long) {
        viewModelScope.launch {
            ingredientRepository.deleteIngredient(id)
            recipesViewModel.refreshIngredients()
        }
    }

//    CATEGORY

    fun loadCategoryValueById(id: Long) {
        if (id == null) {
            _error.value = "ID не указан"
            return
        }
        if (currentCategoryValueId == id) return
        currentCategoryValueId = id
        _loading.value = true

        viewModelScope.launch {
            try {
                val categoryValue = categoryRepository.getCategoryValueById(id)
                nameCategoryValue = categoryValue?.categoryValue ?: ""
                nameCategoryTypeByValue = categoryValue?.typeName ?: ""
                typeIdCategoryValue = categoryValue?.typeId
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCategoryTypeById(id: Long) {
        if (id == null) {
            _error.value = "ID не указан"
            return
        }
        if (currentCategoryTypeId == id) return
        currentCategoryTypeId = id
        _loading.value = true

        viewModelScope.launch {
            try {
                val categoryType = categoryRepository.getCategoryTypeById(id)
                nameCategoryType = categoryType?.nameType ?: ""

            } catch (e: Exception) {
                _error.value = e.message
                Log.e("ADMIN", "AdminViewModel: error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun createCategoryValue(categoryValue: CategoryValueCreate) {
        viewModelScope.launch {
            categoryRepository.createCategoryValues(categoryValue)
            recipesViewModel.refreshCategoryValues()
        }
    }

    fun updateCategoryValue(id: Long, categoryValue: CategoryValueUpdate) {
        viewModelScope.launch {
            categoryRepository.updateCategoryValue(id, categoryValue)
            recipesViewModel.refreshCategoryValues()
        }
    }

    fun deleteCategoryValue(id: Long) {
        viewModelScope.launch {
            categoryRepository.deleteCategoryValue(id)
            recipesViewModel.refreshCategoryValues()
        }
    }
}