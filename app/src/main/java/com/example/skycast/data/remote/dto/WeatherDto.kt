package com.example.skycast.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val current: CurrentDto? = null,
    val hourly: HourlyDto? = null,
    val daily: DailyDto? = null
)

@Serializable
data class CurrentDto(
    val time: String,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("relative_humidity_2m") val humidity: Int? = null,
    @SerialName("precipitation") val precipitation: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("cloud_cover") val cloudCover: Int? = null,
    @SerialName("pressure_msl") val pressure: Double? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("wind_direction_10m") val windDirection: Int? = null,
    @SerialName("is_day") val isDay: Int? = null
)

@Serializable
data class HourlyDto(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperature: List<Double?> = emptyList(),
    @SerialName("apparent_temperature") val apparentTemperature: List<Double?> = emptyList(),
    @SerialName("precipitation_probability") val precipitationProbability: List<Int?> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
    @SerialName("wind_speed_10m") val windSpeed: List<Double?> = emptyList(),
    @SerialName("relative_humidity_2m") val humidity: List<Int?> = emptyList(),
    @SerialName("pressure_msl") val pressure: List<Double?> = emptyList()
)

@Serializable
data class DailyDto(
    val time: List<String> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
    @SerialName("temperature_2m_max") val temperatureMax: List<Double?> = emptyList(),
    @SerialName("temperature_2m_min") val temperatureMin: List<Double?> = emptyList(),
    val sunrise: List<String?> = emptyList(),
    val sunset: List<String?> = emptyList(),
    @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?> = emptyList(),
    @SerialName("uv_index_max") val uvIndexMax: List<Double?> = emptyList()
)
