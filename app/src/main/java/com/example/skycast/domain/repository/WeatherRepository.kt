package com.example.skycast.domain.repository

import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun forecast(city: City, settings: UserSettings): Resource<WeatherForecast>
    suspend fun searchCities(query: String): Resource<List<City>>
}
