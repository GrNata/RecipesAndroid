package com.grig.recipesandroid.data.repository

import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.domain.model.Category

class CategoryRepository(private val api: RecipeApi) {

    suspend fun getCategories(): List<Category> {
        val categoriesDto = api.getCategories()     // API метод возвращает List<CategoryDto>
        return categoriesDto.map { dto ->
            Category(
                id = dto.id ?: 0L,
                name = dto.name,
                image = dto.image ?: ""
                )
        }
    }
}