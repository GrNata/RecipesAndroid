package com.grig.recipesandroid.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    /**
     * Получает email или userId из JWT accessToken.
     * Работает без внешних библиотек.
     */
    fun getEmailFromToken(token: String): String? {
        return try {
            // JWT = header.payload.signature
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = parts[1]
            // Декодируем Base64 URL
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
            val json = JSONObject(String(decoded))
            // возвращаем поле "sub" или "username" (в зависимости от твоего токена)
            json.optString("username", json.optString("sub", null))
        } catch (e: Exception) {
            null
        }
    }

    fun getUserIdFromToken(token: String): Long? {
        val payload = token.split(".")[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
        val json = String(decodedBytes)
        val jsonObject = JSONObject(json)
        return jsonObject.optString("sub").toLongOrNull()
    }

    // Извлекаем payload токена JWT и парсим роли
    fun getRolesFromToken(token: String): Set<String> {
        try {
            val parts = token.split(".")
            if (parts.size != 3) return emptySet()   // JWT = header.payload.signature

            val payloadEncoded = parts[1]
            val payloadBytes = Base64.decode(payloadEncoded, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            val payloadJson = String(payloadBytes)
            val jsonObj = JSONObject(payloadJson)

            val rolesJsonArray = jsonObj.optJSONArray("roles") ?: return emptySet()
            val roles = mutableSetOf<String>()
            for (i in 0 until rolesJsonArray.length()) {
                roles.add(rolesJsonArray.getString(i))
            }
            return roles
        } catch (e: Exception) {
            e.printStackTrace()
            return emptySet()
        }
    }

    // Проверка на истёкший токен (можно использовать при каждом запросе)
    fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payloadEncoded = parts[1]
            val payloadBytes = Base64.decode(payloadEncoded, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
            val payloadJson = String(payloadBytes)
            val jsonObj = JSONObject(payloadJson)

            val exp = jsonObj.optLong("exp", 0L)
            val now = System.currentTimeMillis() / 1000
            exp <= now
        } catch (e: Exception) {
            true
        }
    }

}