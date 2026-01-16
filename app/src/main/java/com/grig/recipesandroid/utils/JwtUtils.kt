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
}