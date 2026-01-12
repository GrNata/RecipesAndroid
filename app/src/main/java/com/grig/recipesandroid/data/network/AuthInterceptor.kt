package com.grig.recipesandroid.data.network

import com.grig.recipesandroid.data.local.TokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

//Цель: автоматически добавлять Authorization: Bearer <accessToken> к защищённым запросам, обновлять accessToken при 401.
//Этот Interceptor подключается при создании Retrofit:

class AuthInterceptor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
//        val accessToken = runBlocking { tokenRepository.accessToken.first() }

        // публичные эндпоинты
        if (url.contains("/api/recipes")) {
            return chain.proceed(originalRequest)
        }

        // Для защищённых эндпоинтов добавляем токен, если он есть

        val accessToken = runBlocking { tokenRepository.accessToken.first() }

        val requestWithToken = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(requestWithToken)

        if (response.code == 401) {
            // TODO: здесь можно синхронно обновить accessToken через refreshToken и повторить запрос
        }
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