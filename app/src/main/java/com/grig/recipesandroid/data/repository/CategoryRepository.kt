package com.grig.recipesandroid.data.repository

import android.util.Log
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.model.dto.CategoryValueDto
import com.grig.recipesandroid.domain.model.Category

class CategoryRepository(private val api: RecipeApi) {

    // Получение всех значений категорий
    suspend fun getCategoryValues(): List<CategoryValueDto> {
//        val categoriesDto = api.getCategories()     // API метод возвращает List<CategoryDto>
        return try {
            val response = api.getCategoryValues()     // // API возвращает List<CategoryValueDto>
            Log.d("CATEGORY", "CategoryRepository loaded ${response.size} category values")
            response
        } catch (e: Exception) {
            Log.e("CATEGORY", "CategoryRepository: Error loading category values", e)
            emptyList()
        }
    }

    // Можно добавить метод по id, если нужно
    suspend fun getCategoryValueById(id: Long): CategoryValueDto? {
        return try {
            api.getCategoryValuesById(id)
        } catch (e: Exception) {
            Log.e("CATEGORY", "CategoryRepository: Error loading category value $id", e)
            null
        }
    }

//    suspend fun getCategories(): List<Category> {
//        val categoriesDto = api.getCategories()     // API метод возвращает List<CategoryDto>
//        return categoriesDto.map { dto ->
//            Category(
//                id = dto.id ?: 0L,
//                name = dto.name,
//                image = dto.image ?: ""
//                )
//        }
//    }
}