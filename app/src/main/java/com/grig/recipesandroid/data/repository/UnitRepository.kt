package com.grig.recipesandroid.data.repository

import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.model.dto.UnitDto
import com.grig.recipesandroid.domain.model.UnitEntity

class UnitRepository(private val api: RecipeApi) {

    suspend fun getUnits() : List<UnitEntity> {
        val unitsDto = api.getUnits()
        return unitsDto.map { dto ->
            UnitEntity(
                id = dto.id ?: 0L,
                code = dto.code,
                label = dto.label
            )
        }
    }

    suspend fun getAllUnits() : List<UnitDto> {
        return api.getUnits()
    }
}