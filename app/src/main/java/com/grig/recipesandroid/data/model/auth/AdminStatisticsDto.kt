package com.grig.recipesandroid.data.model.auth

data class AdminStatisticsDto(
    val totalUsers: Long,
    val totalRecipes: Long,
    val totalIngredients: Long,
    val popularCategoriesValue: List<CategoryStateValue>,
    val topAuthors: List<AuthorStars>
//    val topAuthor: List<AuthorStars> = emptyList()
)

data class CategoryStateValue(
    val categoryValueName: String,
    val recipeCount: Long
)

data class AuthorStars(
    val authorId: Long,
    val username: String,
    val recipeCount: Long
)
