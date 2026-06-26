package com.example.skycast.domain.repository

import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settings: Flow<UserSettings>
    val savedCities: Flow<List<City>>

    suspend fun updateSettings(settings: UserSettings)
    suspend fun saveCity(city: City)
    suspend fun deleteCity(city: City)
    suspend fun clearCities()
}
