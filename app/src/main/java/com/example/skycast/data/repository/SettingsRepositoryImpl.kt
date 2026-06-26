package com.example.skycast.data.repository

import com.example.skycast.data.local.SavedCitiesDataStore
import com.example.skycast.data.local.SettingsDataStore
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val settingsDataStore: SettingsDataStore,
    private val savedCitiesDataStore: SavedCitiesDataStore
) : SettingsRepository {
    override val settings: Flow<UserSettings> = settingsDataStore.settings
    override val savedCities: Flow<List<City>> = savedCitiesDataStore.savedCities

    override suspend fun updateSettings(settings: UserSettings) {
        settingsDataStore.update(settings)
    }

    override suspend fun saveCity(city: City) {
        savedCitiesDataStore.save(city)
    }

    override suspend fun deleteCity(city: City) {
        savedCitiesDataStore.delete(city)
    }

    override suspend fun clearCities() {
        savedCitiesDataStore.clear()
    }
}
