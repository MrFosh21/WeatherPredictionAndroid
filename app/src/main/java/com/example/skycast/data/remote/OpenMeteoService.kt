package com.example.skycast.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object OpenMeteoService {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun weatherApi(): OpenMeteoApi = retrofit("https://api.open-meteo.com/").create(OpenMeteoApi::class.java)

    fun geocodingApi(): OpenMeteoApi = retrofit("https://geocoding-api.open-meteo.com/").create(OpenMeteoApi::class.java)

    private fun retrofit(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
