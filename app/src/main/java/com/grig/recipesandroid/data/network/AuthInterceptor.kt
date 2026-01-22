package com.grig.recipesandroid.data.network

import android.util.Log
import com.grig.recipesandroid.data.local.TokenRepository
import com.grig.recipesandroid.data.repository.AuthRepository
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

        // Для защищённых эндпоинтов добавляем токен, если он есть
        val accessToken = runBlocking { tokenRepository.accessToken.first() }

//        Публичные эндпоинты — без токена
//        val isPublicEndpoint =
//                    url.contains("/api/recipes") ||
//                    url.contains("/api/recipe/") ||
//                    url.contains("/api/auth/")
//        val isPublicEndpoint =
//                    url.endsWith("/api/recipes") ||          // список рецептов
//                    url.endsWith("/api/recipes/search") ||
//                    (url.contains("/api/recipes/") && !url.contains("/my/")) ||
//                    url.startsWith("http://10.0.2.2:9090/api/auth/")
        val isPublicEndpoint =
            (originalRequest.method == "GET" &&
                    (
                            url.endsWith("/api/recipes") ||
                            url.endsWith("/api/recipes/search") ||
                            url.contains("/api/recipes/") && !url.contains("/my/"))) ||
                    url.startsWith("http://10.0.2.2:9090/api/auth/")

        // 👉 если public — всегда без токена
        if (isPublicEndpoint) {
            Log.d("MY Recipes token", "PUBLIC $url")
            return chain.proceed(originalRequest)
        }
        // 👉 если НЕ public, но токена нет — тоже без токена (сервер вернёт 401)
        if (accessToken.isNullOrBlank()) {
            Log.d("MY Recipes token", "NO TOKEN $url")
            return chain.proceed(originalRequest)
        }
        Log.d("MY Recipes token", "accessToken: $accessToken")
        // 👉 защищённый эндпоинт + токен  - Добавляем токен в заголовок
        val requestWithToken = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        Log.d("MY Recipes token - HTTP AuthInterceptor", "URL=${requestWithToken.url} headers=${requestWithToken.headers}")

        var response = chain.proceed(requestWithToken)


// Если accessToken невалидный / истёк
//        if (response.code == 401 || response.code == 403) {
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

            if (!newAccessToken.isNullOrBlank()) {
                // повторяем запрос с новым токеном
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $newAccessToken")
                    .build()
                Log.d("MY Recipes token", "URL=${newRequest.url} headers=${newRequest.headers}")

                response.close()  // закрываем старый response

                response = chain.proceed(newRequest)
            }
            // если обновить не получилось — отдаем оригинальный ответ (401/403)
        }

        Log.d("MY Recipes token", "URL=$url token=${accessToken?.take(10)}")

        return response
    }
}

//Этот Interceptor подключается при создании Retrofit:
//  val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor(AuthInterceptor(tokenRepository))
//    .build()
//
//  val retrofit = Retrofit.Builder()
//    .baseUrl("http://10.0.2.2:9090/")
//    .client(okHttpClient)
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()