package com.example.skycast.domain

import com.example.skycast.domain.intelligence.ForecastConfidenceCalculator
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.WeatherCondition
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class ForecastConfidenceCalculatorTest {
    private val calculator = ForecastConfidenceCalculator()

    @Test
    fun nearTermStableForecastHasHighConfidence() {
        val hours = (0 until 12).map {
            hour(temp = 20.0 + it * 0.1, rain = 10, condition = WeatherCondition.CLEAR)
        }
        assertTrue(calculator.confidenceForDay(0, hours).score >= 88)
    }

    @Test
    fun middleRainAndChangingWeatherReduceConfidence() {
        val hours = (0 until 12).map {
            hour(temp = if (it % 2 == 0) 12.0 else 28.0, rain = 50, condition = if (it % 2 == 0) WeatherCondition.RAIN else WeatherCondition.CLEAR)
        }
        assertTrue(calculator.confidenceForDay(3, hours).score < 76)
    }

    private fun hour(temp: Double, rain: Int, condition: WeatherCondition) = HourlyForecast(
        time = LocalDateTime.of(2026, 6, 25, 12, 0),
        temperatureC = temp,
        apparentTemperatureC = temp,
        condition = condition,
        precipitationProbability = rain,
        windSpeedKmh = 8.0,
        humidityPercent = 55,
        pressureHpa = 1012.0
    )
}
