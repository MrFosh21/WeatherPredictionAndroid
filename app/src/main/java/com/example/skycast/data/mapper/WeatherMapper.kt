package com.example.skycast.data.mapper

import com.example.skycast.core.util.DateFormatterHelper
import com.example.skycast.data.remote.dto.DailyDto
import com.example.skycast.data.remote.dto.HourlyDto
import com.example.skycast.data.remote.dto.WeatherDto
import com.example.skycast.domain.intelligence.WeatherInsightEngine
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.CurrentWeather
import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.domain.model.WeatherForecast
import java.time.LocalDate
import java.time.LocalDateTime

class WeatherMapper(
    private val insightEngine: WeatherInsightEngine = WeatherInsightEngine()
) {
    fun fromDto(dto: WeatherDto, city: City): WeatherForecast {
        val hourly = mapHourly(dto.hourly).take(240)
        val daily = mapDaily(dto.daily, hourly)
        val current = dto.current?.let { current ->
            CurrentWeather(
                time = safeDateTime(current.time) ?: LocalDateTime.now(),
                temperatureC = current.temperature ?: hourly.firstOrNull()?.temperatureC ?: 0.0,
                apparentTemperatureC = current.apparentTemperature ?: current.temperature ?: 0.0,
                condition = WeatherCondition.fromOpenMeteoCode(current.weatherCode),
                windSpeedKmh = current.windSpeed ?: 0.0,
                windDirectionDegrees = current.windDirection ?: 0,
                humidityPercent = current.humidity ?: hourly.firstOrNull()?.humidityPercent ?: 0,
                pressureHpa = current.pressure,
                cloudCoverPercent = current.cloudCover,
                precipitationMm = current.precipitation,
                isDay = current.isDay != 0
            )
        } ?: fallbackCurrent(hourly)

        val insight = insightEngine.buildInsight(
            condition = current.condition,
            currentTempC = current.temperatureC,
            apparentTempC = current.apparentTemperatureC,
            windKmh = current.windSpeedKmh,
            hourly = hourly,
            daily = daily
        )
        return WeatherForecast(
            city = city,
            current = current,
            hourly = hourly,
            daily = daily,
            insight = insight,
            timezone = dto.timezone
        )
    }

    private fun mapHourly(dto: HourlyDto?): List<HourlyForecast> {
        if (dto == null) return emptyList()
        return dto.time.mapIndexedNotNull { index, rawTime ->
            val time = safeDateTime(rawTime) ?: return@mapIndexedNotNull null
            HourlyForecast(
                time = time,
                temperatureC = dto.temperature.getOrNull(index) ?: return@mapIndexedNotNull null,
                apparentTemperatureC = dto.apparentTemperature.getOrNull(index) ?: dto.temperature.getOrNull(index) ?: 0.0,
                condition = WeatherCondition.fromOpenMeteoCode(dto.weatherCode.getOrNull(index)),
                precipitationProbability = dto.precipitationProbability.getOrNull(index) ?: 0,
                windSpeedKmh = dto.windSpeed.getOrNull(index) ?: 0.0,
                humidityPercent = dto.humidity.getOrNull(index) ?: 0,
                pressureHpa = dto.pressure.getOrNull(index)
            )
        }
    }

    private fun mapDaily(dto: DailyDto?, hourly: List<HourlyForecast>): List<DailyForecast> {
        if (dto == null) return emptyList()
        return dto.time.mapIndexedNotNull { index, rawDate ->
            val date = safeDate(rawDate) ?: return@mapIndexedNotNull null
            val dayHours = hourly.filter { it.time.toLocalDate() == date }
            val high = dto.temperatureMax.getOrNull(index) ?: dayHours.maxOfOrNull { it.temperatureC } ?: return@mapIndexedNotNull null
            val low = dto.temperatureMin.getOrNull(index) ?: dayHours.minOfOrNull { it.temperatureC } ?: high
            DailyForecast(
                date = date,
                condition = WeatherCondition.fromOpenMeteoCode(dto.weatherCode.getOrNull(index)),
                highC = high,
                lowC = low,
                precipitationProbability = dto.precipitationProbabilityMax.getOrNull(index) ?: 0,
                uvIndex = dto.uvIndexMax.getOrNull(index),
                sunrise = dto.sunrise.getOrNull(index)?.let(::safeDateTime),
                sunset = dto.sunset.getOrNull(index)?.let(::safeDateTime),
                confidence = insightEngine.confidenceForDay(index, dayHours)
            )
        }
    }

    private fun fallbackCurrent(hourly: List<HourlyForecast>): CurrentWeather {
        val first = hourly.firstOrNull()
        return CurrentWeather(
            time = first?.time ?: LocalDateTime.now(),
            temperatureC = first?.temperatureC ?: 0.0,
            apparentTemperatureC = first?.apparentTemperatureC ?: first?.temperatureC ?: 0.0,
            condition = first?.condition ?: WeatherCondition.UNKNOWN,
            windSpeedKmh = first?.windSpeedKmh ?: 0.0,
            windDirectionDegrees = 0,
            humidityPercent = first?.humidityPercent ?: 0,
            pressureHpa = first?.pressureHpa,
            cloudCoverPercent = null,
            precipitationMm = null,
            isDay = true
        )
    }

    private fun safeDateTime(value: String): LocalDateTime? =
        runCatching { DateFormatterHelper.parseDateTime(value) }.getOrNull()

    private fun safeDate(value: String): LocalDate? =
        runCatching { DateFormatterHelper.parseDate(value) }.getOrNull()
}
