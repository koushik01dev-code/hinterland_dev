package com.hinterland.app.components.network

import com.hinterland.app.utils.SharedStateUtils
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class RetrofitWrapper private constructor() {

    private fun resolvedBaseUrl(): String {
        val url = SharedStateUtils.baseUrl
        return if (url.isNotBlank()) {
            if (url.endsWith('/')) url else "$url/"
        } else DEFAULT_BASE_URL
    }

    private val logging: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(resolvedBaseUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createClient(type: Type, typeAdapter: Any): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(type, typeAdapter)
            .create()
        return Retrofit.Builder()
            .baseUrl(resolvedBaseUrl())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun setBaseUrl(url: String) {
        if (url.isNotBlank()) {
            SharedStateUtils.baseUrl = if (url.endsWith('/')) url else "$url/"
        }
    }

    companion object {
        private const val DEFAULT_BASE_URL = "http://localhost:8080/api"
        val instance: RetrofitWrapper by lazy { RetrofitWrapper() }
    }
}


