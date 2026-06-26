package com.example.skycast.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.skycast.data.remote.dto.WeatherDto
import com.example.skycast.domain.model.City
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.weatherCacheStore by preferencesDataStore(name = "skycast_weather_cache")

class WeatherCacheDataStore(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    suspend fun save(city: City, weather: WeatherDto) {
        context.weatherCacheStore.edit { preferences ->
            preferences[keyFor(city)] = json.encodeToString(weather)
        }
    }

    suspend fun get(city: City): WeatherDto? =
        context.weatherCacheStore.data.map { preferences ->
            preferences[keyFor(city)]?.let { raw ->
                runCatching { json.decodeFromString<WeatherDto>(raw) }.getOrNull()
            }
        }.first()

    private fun keyFor(city: City) = stringPreferencesKey("weather_${city.id}_${city.latitude}_${city.longitude}")
}
