package com.example.skycast.domain

import com.example.skycast.domain.intelligence.WeatherAdviceEngine
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.WeatherCondition
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class WeatherAdviceEngineTest {
    private val engine = WeatherAdviceEngine()

    @Test
    fun recommendsJacketForCoolWeather() {
        assertTrue(engine.outfitSuggestion(8.0, 3.0, 10, 8.0).contains("jacket", ignoreCase = true))
    }

    @Test
    fun detectsBestLowRiskHour() {
        val hours = listOf(
            hour(9, rain = 70, wind = 12.0),
            hour(14, rain = 5, wind = 6.0),
            hour(18, rain = 30, wind = 9.0)
        )
        assertTrue(engine.bestTimeOfDay(hours).contains("14:00"))
    }

    private fun hour(hour: Int, rain: Int, wind: Double) = HourlyForecast(
        time = LocalDateTime.of(2026, 6, 25, hour, 0),
        temperatureC = 21.0,
        apparentTemperatureC = 21.0,
        condition = WeatherCondition.PARTLY_CLOUDY,
        precipitationProbability = rain,
        windSpeedKmh = wind,
        humidityPercent = 55,
        pressureHpa = 1010.0
    )
}
