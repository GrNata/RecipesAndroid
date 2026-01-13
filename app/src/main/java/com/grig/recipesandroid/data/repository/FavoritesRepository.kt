package com.grig.recipesandroid.data.repository

import android.util.Log
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.model.dto.Favorite
import com.grig.recipesandroid.data.model.dto.RecipeDto

class FavoritesRepository(
    private val api: RecipeApi,
    private val tokenRepository: TokenRepository
) {

    suspend fun addToFavorites(recipeId: Long) {
        api.addFavorite(recipeId)
    }

    suspend fun removeFromFavorites(recipeId: Long) {
        api.removeFavorite(recipeId)
    }

    suspend fun getFavorites(): Set<Long> {
        return api.getFavoritesAll().mapNotNull { it.id }.toSet()  // // получаем список id рецептов
    }
//    suspend fun getFavorites(): List<Long> {
//        return api.getFavoritesAll().map { it.recipeId }  // // получаем список id рецептов
////        return api.getFavoritesAll().map { it.id as Long }  // // получаем список id рецептов
//    }


    suspend fun getTempFavorites(): List<RecipeDto> {
        return api.getFavoritesAll()
    }
}