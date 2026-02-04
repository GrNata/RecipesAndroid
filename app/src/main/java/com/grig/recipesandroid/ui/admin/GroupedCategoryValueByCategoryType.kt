package com.grig.recipesandroid.ui.admin

import android.util.Log
import com.grig.recipesandroid.data.model.dto.CategoryValueDto

fun GroupedCategoryValueByCategoryType(
    selectCategoryTypeId: Long,
    categoriesValueAll: List<CategoryValueDto>
): List<CategoryValueDto> {

    Log.d("ADMIN", "GrupedCategory selectCategoryType: $selectCategoryTypeId, categoriesValueAll: $categoriesValueAll")

    return categoriesValueAll.filter { it.typeId == selectCategoryTypeId }

}