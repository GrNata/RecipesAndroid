package com.grig.recipesandroid.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_token")

object TokenStorage {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
}

class TokenRepository(
    private val context: Context
) {
    val accessToken: Flow<String?> = context.dataStore.data
        .map { it[TokenStorage.ACCESS_TOKEN_KEY] }

    val refreshToken: Flow<String?> = context.dataStore.data
        .map { it[TokenStorage.REFRESH_TOKEN_KEY] }

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit { pefs ->
            pefs[TokenStorage.ACCESS_TOKEN_KEY] = access
            pefs[TokenStorage.REFRESH_TOKEN_KEY] = refresh
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(TokenStorage.ACCESS_TOKEN_KEY)
            prefs[TokenStorage.REFRESH_TOKEN_KEY]
        }
    }
}

//•	В RestApiRecipes refresh_token хранится 30 дней, accessToken короткий.
//	•	На Android мы будем хранить их в DataStore и при необходимости обновлять accessToken через refreshToken.

