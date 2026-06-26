package com.example.skycast.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.skycast.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

private val Context.savedCitiesStore by preferencesDataStore(name = "skycast_saved_cities")

class SavedCitiesDataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val listSerializer = ListSerializer(City.serializer())

    val savedCities: Flow<List<City>> = context.savedCitiesStore.data.map { preferences ->
        preferences[Keys.CITIES]?.let { raw ->
            runCatching { json.decodeFromString(listSerializer, raw) }.getOrDefault(emptyList())
        } ?: emptyList()
    }

    suspend fun save(city: City) {
        val current = savedCities.first()
        val next = (listOf(city) + current.filterNot { it.id == city.id }).take(12)
        write(next)
    }

    suspend fun delete(city: City) {
        write(savedCities.first().filterNot { it.id == city.id })
    }

    suspend fun clear() {
        write(emptyList())
    }

    private suspend fun write(cities: List<City>) {
        context.savedCitiesStore.edit { preferences ->
            preferences[Keys.CITIES] = json.encodeToString(listSerializer, cities)
        }
    }

    private object Keys {
        val CITIES = stringPreferencesKey("cities")
    }
}
