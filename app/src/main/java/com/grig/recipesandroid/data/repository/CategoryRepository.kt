package com.grig.recipesandroid.data.repository

import android.util.Log
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.model.dto.CategoryTypeRequest
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto
import com.grig.recipesandroid.data.model.dto.CategoryTypeUpdate
import com.grig.recipesandroid.data.model.dto.CategoryValueRequest
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import okhttp3.Response

//import com.grig.recipesandroid.domain.model.Category

class CategoryRepository(private val api: RecipeApi) {

    // ++++++++++ CategoryValue
    suspend fun getCategoryValues(): List<CategoryValueDto> {
//        val categoriesDto = api.getCategories()     // API метод возвращает List<CategoryDto>
        return try {
            val response = api.getCategoryValues()     // // API возвращает List<CategoryValueDto>
            Log.d("CATEGORY-ch", "CategoryRepository loaded ${response.size} category values")
            response
        } catch (e: Exception) {
            Log.e("CATEGORY-ch", "CategoryRepository: Error loading category values", e)
            emptyList()
        }
    }

    // Можно добавить метод по id, если нужно
    suspend fun getCategoryValueById(id: Long): CategoryValueDto? {
        return try {
            api.getCategoryValuesById(id)
        } catch (e: Exception) {
            Log.e("CATEGORY-ch", "CategoryRepository: Error loading category value $id", e)
            null
        }
    }

    suspend fun createCategoryValues(categoryValues: CategoryValueRequest) = api.createCategoryValue(categoryValues)


    suspend fun updateCategoryValue(id: Long, categoryValue: CategoryValueRequest) = api.updateCategoryValue(id, categoryValue)

    suspend fun deleteCategoryValue(id: Long) = api.deleteCategoryValue(id)

//    ++++++++++++++++++
//    +++++++   CategoryType

    suspend fun getCategoryTypes(): List<CategoryTypeDto> {
        return try {
            Log.d("SEARCH INGREDIENT", "CategoryRepository: START loaded category type")
            val response = api.getCategoryTypes()
            Log.d("SEARCH INGREDIENT", "CategoryRepository loaded ${response} category type")
            response
        } catch (e: Exception) {
            Log.e("SEARCH INGREDIENT", "CategoryRepository: Error loading category type", e)
            emptyList()
        }
    }

    suspend fun getCategoryTypeById(id: Long): CategoryTypeDto? {
        return try {
            api.getCategoryTypeById(id)
        } catch (e: Exception) {
            Log.e("CATEGORY-ch", "CategoryRepository: Error loading category type", e)
            null
        }
    }

    suspend fun createCategoryType(categoryType: CategoryTypeRequest) = api.createCategoryType(categoryType)


    suspend fun updateCategoryType(id: Long, categoryType: CategoryTypeRequest)  {
        Log.d("ADMIN", "CategoryRepository: update id=$id, categoryType=${categoryType.nameType}")
        api.updateCategoryType(id, categoryType)
    }


    suspend fun deleteCategoryType(id: Long) = api.deleteCategoryType(id)

}