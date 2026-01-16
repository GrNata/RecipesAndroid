package com.grig.recipesandroid.data.repository

import android.util.Log
import com.grig.recipesandroid.data.api.RecipeApi
import com.grig.recipesandroid.data.local.FavoritesDataStore
import com.grig.recipesandroid.data.local.TokenRepository
import kotlinx.coroutines.flow.first

class FavoritesRepository(
    private val api: RecipeApi,
    private val tokenRepository: TokenRepository,
    private val local: FavoritesDataStore       //  объединяем local + remote
) {

//    1.	Избранное хранится локально
//	•	даже без интернета
//	•	даже после перезапуска приложения
//	2.	При запуске приложения
//	•	сразу показываем локальные избранные
//	•	если есть токен → синхронизируем с сервером
//	•	сервер становится «истиной», но UI не ждёт
//    Это называется offline-first + eventual consistency.

//    val localFavoritesFlow = local.favoritesFlow
//    // теперь поток для конкретного пользователя
    fun localFavoritesFlow(userId: String?) = local.favoritesFlowForUser(userId)
    suspend fun addToFavorites(recipeId: Long, userId: String?) {
        api.addFavorite(recipeId)
        //  объединяем local + remote
        val current = localFavoritesFlow(userId).first()
        local.saveFavorites(current + recipeId, userId)
    }

    suspend fun removeFromFavorites(recipeId: Long, userId: String?) {
        api.removeFavorite(recipeId)
        //  объединяем local + remote
        val current = localFavoritesFlow(userId).first()
        local.saveFavorites(current - recipeId, userId)
    }

    suspend fun getFavoritesFromServer(): Set<Long> {
        return api.getFavoritesAll().mapNotNull { it.id }.toSet()  // // получаем список id рецептов
    }

    suspend fun saveFavoritesToLocal(userId: String?, ids: Set<Long>) {
        local.saveFavorites(ids, userId)
    }

    //  объединяем local + remote

    suspend fun loadFromServerAndSync() {
        val remoteRecipes = api.getFavoritesAll()
        val ids = remoteRecipes.map { it.id }.toSet()
    }

    suspend fun clearLocal(userId: String?) {
        local.clear(userId)
    }

    suspend fun syncFavoritesWithServer(userId: String?) {
//        1. серверные избранные
        val serverFavorites = getFavoritesFromServer()
//        2. локальные избранные
        val localFavorites = local.favoritesFlowForUser(userId).first()
//        3. что есть локально, но нет на сервере
        val toUpload = localFavorites - serverFavorites
//        4. pfuhe;ftv ytljcnf.obt yf cthdth
        toUpload.forEach { recipeId ->
            try {
                api.addFavorite(recipeId)
            } catch (e: Exception) {
                Log.e("FavoritesSync", "Sync failed, will retry next login", e)
                throw e // ← прервёт sync и повторится при следующем входе
            }
        }
//        5. итог = север и локальные
        val merge = serverFavorites + localFavorites
//         6. сохраняем итог локально
        local.saveFavorites(merge, userId)
    }

}