package com.example.skycast.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class WeatherForecast(
    val city: City,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>,
    val insight: WeatherInsight,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val timezone: String = "auto"
)

data class CurrentWeather(
    val time: LocalDateTime,
    val temperatureC: Double,
    val apparentTemperatureC: Double,
    val condition: WeatherCondition,
    val windSpeedKmh: Double,
    val windDirectionDegrees: Int,
    val humidityPercent: Int,
    val pressureHpa: Double?,
    val cloudCoverPercent: Int?,
    val precipitationMm: Double?,
    val isDay: Boolean
)

data class HourlyForecast(
    val time: LocalDateTime,
    val temperatureC: Double,
    val apparentTemperatureC: Double,
    val condition: WeatherCondition,
    val precipitationProbability: Int,
    val windSpeedKmh: Double,
    val humidityPercent: Int,
    val pressureHpa: Double?
)

data class DailyForecast(
    val date: LocalDate,
    val condition: WeatherCondition,
    val highC: Double,
    val lowC: Double,
    val precipitationProbability: Int,
    val uvIndex: Double?,
    val sunrise: LocalDateTime?,
    val sunset: LocalDateTime?,
    val confidence: ForecastConfidence
)

data class ForecastConfidence(
    val score: Int,
    val explanation: String
)

data class WeatherInsight(
    val summary: String,
    val trend: String,
    val rainWarning: String?,
    val windWarning: String?,
    val outfitSuggestion: String,
    val bestTime: String,
    val confidence: ForecastConfidence
)
