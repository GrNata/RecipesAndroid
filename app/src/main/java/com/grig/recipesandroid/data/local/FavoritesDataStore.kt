package com.grig.recipesandroid.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "favorites_store")

class FavoritesDataStore(private val context: Context) {

    private val FAVORITES_KEY_PREFIX = stringSetPreferencesKey("favorites_ids")
    private val USER_KEY = stringSetPreferencesKey("current_user")      // для хранения текущего email или userId
//    	•	FAVORITES_KEY_PREFIX — префикс, к которому будем добавлять userId.
//	    •	USER_KEY — хранит, кто сейчас залогинен.

    fun favoritesFlowForUser(userId: String?) : Flow<Set<Long>> {
        val key = stringSetPreferencesKey("${FAVORITES_KEY_PREFIX}_${userId ?: "anonymous"}")
        return context.dataStore.data.map { preferences ->
            preferences[key]
                ?.mapNotNull { it.toLongOrNull() }
                ?.toSet()
                ?: emptySet()
        }
    }
//    val favoritesFlow: Flow<Set<Long>> = context.dataStore.data.map { preferences ->
//        preferences[FAVORITES_KEY]
//            ?.mapNotNull { it.toLongOrNull() }
//            ?.toSet()
//            ?: emptySet()
//    }

    suspend fun saveFavorites(ids: Set<Long>, userId: String?) {
        val key = stringSetPreferencesKey("${FAVORITES_KEY_PREFIX}_${userId ?: "anonymous"}")
        context.dataStore.edit { preferences ->
            preferences[key] = ids.map { it.toString() }.toSet()
        }
    }

    suspend fun clear(userId: String?) {
        val key = stringSetPreferencesKey("${FAVORITES_KEY_PREFIX}_${userId ?: "anonymous"}")
        context.dataStore.edit { preferences ->
            preferences.remove(key) }
    }

}