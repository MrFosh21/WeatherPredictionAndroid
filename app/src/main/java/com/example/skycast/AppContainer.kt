package com.example.skycast

import android.content.Context
import com.example.skycast.data.local.SavedCitiesDataStore
import com.example.skycast.data.local.SettingsDataStore
import com.example.skycast.data.local.WeatherCacheDataStore
import com.example.skycast.data.remote.OpenMeteoService
import com.example.skycast.data.repository.LocationRepositoryImpl
import com.example.skycast.data.repository.SettingsRepositoryImpl
import com.example.skycast.data.repository.WeatherRepositoryImpl
import com.example.skycast.domain.repository.LocationRepository
import com.example.skycast.domain.repository.SettingsRepository
import com.example.skycast.domain.repository.WeatherRepository

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val settingsDataStore = SettingsDataStore(appContext)
    private val savedCitiesDataStore = SavedCitiesDataStore(appContext)
    private val weatherCacheDataStore = WeatherCacheDataStore(appContext)

    val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        weatherApi = OpenMeteoService.weatherApi(),
        geocodingApi = OpenMeteoService.geocodingApi(),
        cache = weatherCacheDataStore
    )

    val settingsRepository: SettingsRepository = SettingsRepositoryImpl(
        settingsDataStore = settingsDataStore,
        savedCitiesDataStore = savedCitiesDataStore
    )

    val locationRepository: LocationRepository = LocationRepositoryImpl(appContext)
}
