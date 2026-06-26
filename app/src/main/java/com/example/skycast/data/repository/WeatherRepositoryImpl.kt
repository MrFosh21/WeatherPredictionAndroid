package com.example.skycast.data.repository

import com.example.skycast.core.result.Resource
import com.example.skycast.data.local.WeatherCacheDataStore
import com.example.skycast.data.mapper.CityMapper
import com.example.skycast.data.mapper.WeatherMapper
import com.example.skycast.data.remote.OpenMeteoApi
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherForecast
import com.example.skycast.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val weatherApi: OpenMeteoApi,
    private val geocodingApi: OpenMeteoApi,
    private val cache: WeatherCacheDataStore,
    private val mapper: WeatherMapper = WeatherMapper()
) : WeatherRepository {
    override suspend fun forecast(city: City, settings: UserSettings): Resource<WeatherForecast> =
        withContext(Dispatchers.IO) {
            runCatching {
                val dto = weatherApi.forecast(
                    latitude = city.latitude,
                    longitude = city.longitude
                )
                cache.save(city, dto)
                Resource.Success(mapper.fromDto(dto, city))
            }.getOrElse { error ->
                val cached = cache.get(city)
                if (cached != null) {
                    Resource.Success(mapper.fromDto(cached, city), fromCache = true)
                } else {
                    Resource.Error("Could not load the forecast. Check your connection and try again.", error)
                }
            }
        }

    override suspend fun searchCities(query: String): Resource<List<City>> =
        withContext(Dispatchers.IO) {
            if (query.length < 2) return@withContext Resource.Success(emptyList())
            runCatching {
                geocodingApi.searchCities(query).results.map(CityMapper::fromDto)
            }.fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { Resource.Error("City search failed. Try a different spelling.", it) }
            )
        }
}
