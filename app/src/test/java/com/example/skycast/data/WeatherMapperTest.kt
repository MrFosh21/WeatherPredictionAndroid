package com.example.skycast.data

import com.example.skycast.data.mapper.WeatherMapper
import com.example.skycast.data.remote.dto.CurrentDto
import com.example.skycast.data.remote.dto.DailyDto
import com.example.skycast.data.remote.dto.HourlyDto
import com.example.skycast.data.remote.dto.WeatherDto
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {
    @Test
    fun mapsDtoToDomainForecast() {
        val dto = WeatherDto(
            latitude = 45.5,
            longitude = -73.5,
            timezone = "America/Toronto",
            current = CurrentDto(
                time = "2026-06-25T12:00",
                temperature = 24.0,
                apparentTemperature = 27.0,
                humidity = 64,
                weatherCode = 2,
                windSpeed = 14.0,
                windDirection = 180,
                isDay = 1
            ),
            hourly = HourlyDto(
                time = listOf("2026-06-25T12:00"),
                temperature = listOf(24.0),
                apparentTemperature = listOf(27.0),
                precipitationProbability = listOf(15),
                weatherCode = listOf(2),
                windSpeed = listOf(14.0),
                humidity = listOf(64),
                pressure = listOf(1014.0)
            ),
            daily = DailyDto(
                time = listOf("2026-06-25"),
                weatherCode = listOf(2),
                temperatureMax = listOf(28.0),
                temperatureMin = listOf(18.0),
                sunrise = listOf("2026-06-25T05:05"),
                sunset = listOf("2026-06-25T20:47"),
                precipitationProbabilityMax = listOf(20),
                uvIndexMax = listOf(6.0)
            )
        )

        val forecast = WeatherMapper().fromDto(dto, City("1", "Montreal", "Canada", "Quebec", 45.5, -73.5))

        assertEquals("Montreal", forecast.city.name)
        assertEquals(WeatherCondition.PARTLY_CLOUDY, forecast.current.condition)
        assertEquals(1, forecast.hourly.size)
        assertEquals(1, forecast.daily.size)
        assertEquals(28.0, forecast.daily.first().highC, 0.0)
    }
}
