package com.grig.recipesandroid.data.network

import android.util.Log
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.repository.AuthRepository
import com.grig.recipesandroid.utils.JwtUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

//Цель: автоматически добавлять Authorization: Bearer <accessToken> к защищённым запросам, обновлять accessToken при 401.
//Этот Interceptor подключается при создании Retrofit:

class AuthInterceptor(
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        val method = originalRequest.method

        // Для защищённых эндпоинтов добавляем токен, если он есть
         Log.d("MY Recipes token", "AuthInterceptor - before runBlocking, URL=$url")
        val accessToken = runBlocking { tokenRepository.accessToken.first() }
        Log.d("MY Recipes token", "AuthInterceptor - after runBlocking, accessToken=$accessToken, URL=$url")

//      1  Публичные эндпоинты — без токена
        val isPublicEndpoint =
            (originalRequest.method == "GET" && (
                            url.endsWith("/api/recipes") ||
                            url.endsWith("/api/recipes/search") ||

                                    url.contains("searchByIngredients")
//                                    ||
//                            url.contains("/api/recipes/")
//                                    && !url.contains("/my/")
//                                    && !url.contains("/moderation") // добавлено исключительно для модерации
                            )) ||
                    url.startsWith("http://10.0.2.2:9090/api/auth/")    // login/register

        // 👉 если public — всегда без токена
        if (isPublicEndpoint) {
            Log.d("MY Recipes token", "PUBLIC $url")
            return chain.proceed(originalRequest)
        }

        // 👉2. Если токена нет или он истёк — 401/403 будет с сервера
        if (accessToken.isNullOrBlank() || JwtUtils.isTokenExpired(accessToken)) {
            Log.d("MY Recipes token", "NO TOKEN $url")
            return chain.proceed(originalRequest)
        }
        Log.d("MY Recipes token", "accessToken: $accessToken")

        // 5. Добавляем токен к запросу
        val requestWithToken = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

////        +++++++++++++++++++
//        // 3. Получаем роли из токена
//        val roles = JwtUtils.getRolesFromToken(accessToken)
//        val isAdmin = roles.contains("ADMIN")
//        val isModerator = roles.contains("MODERATOR")
//        val isUser = roles.contains("USER")
//
//        // 4. Проверка прав на эндпоинт
//        val isUserEndpoint = (
//                method == "POST" && url.contains("/api/recipes") ||
//                        method == "PUT" && url.contains("/api/recipes/") ||
//                        method == "DELETE" && url.contains("/api/recipes/") ||
//                        url.contains("/api/favorites") ||
//                        url.contains("/api/units") ||
//                        url.contains("/api/ingredients") ||
//                        url.contains("/api/category-type") ||
//                        url.contains("/api/category-value") ||
//                        url.contains("/api/auth/logout")
//                )
//
//        val isModeratorEndpoint = (
//                method == "GET" && url.contains("/api/recipes/moderation/pending") ||
//                        method == "PATCH" && url.contains("/api/recipes/") && (
//                        url.contains("/send-to-moderation") ||
//                                url.contains("/approve") ||
//                                url.contains("/reject")
//                        )
//                )
//
//        val isAdminEndpoint = url.contains("/api/admin") || url.contains("/api/users")
//
//        val allowed = when {
//            isAdmin -> true // Admin может всё
//            isModerator -> isUserEndpoint || isModeratorEndpoint
//            isUser -> isUserEndpoint
//            else -> false
//        }
//
//        if (!allowed) {
//            throw IllegalAccessException("Access denied for endpoint: $url")
//        }
//
////        // 5. Добавляем токен к запросу
////        val requestWithToken = originalRequest.newBuilder()
////            .addHeader("Authorization", "Bearer $accessToken")
////            .build()
//
////        return chain.proceed(newRequest)
////        +++++++++++++++++++

        var response = chain.proceed(requestWithToken)


//      6 Если accessToken невалидный / истёк, пробуем обновить через refreshToken
        if (response.code == 401 && originalRequest.headers("X-Refresh") == null) {
            // TODO: здесь можно синхронно обновить accessToken через refreshToken и повторить запрос
            // попробуем обновить токен через refreshToken
            val newAccessToken = runBlocking {
                try {
                    authRepository.refreshToken()       // возвращает новый accessToken и сохраняет его
                } catch (e: Exception) {
                    Log.e("MY Recipes token", "ошибка обновления токена e: $e")
                    null
                }
            }
            Log.i("MY Recipes token", "newAccessToken: $newAccessToken")


            if (newAccessToken != null && !newAccessToken.accessToken.isNullOrBlank()) {
                // повторяем запрос с новым токеном
                val retryRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${newAccessToken.accessToken}")
                    .build()
                Log.d("MY Recipes token", "URL=${retryRequest.url} headers=${retryRequest.headers}")

                response.close()  // закрываем старый response
                response = chain.proceed(retryRequest)
            }
            // если обновить не получилось — отдаем оригинальный ответ (401/403)
        }
        return response
    }
}
