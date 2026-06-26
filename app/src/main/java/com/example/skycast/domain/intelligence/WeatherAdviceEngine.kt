package com.example.skycast.domain.intelligence

import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.HourlyForecast

class WeatherAdviceEngine {
    fun outfitSuggestion(currentTempC: Double, maxUv: Double?, rainProbability: Int, windKmh: Double): String {
        val clothing = when {
            currentTempC < 5 -> "Wear warm layers"
            currentTempC < 15 -> "Bring a jacket"
            currentTempC > 28 -> "Keep it light and hydrate"
            else -> "Comfortable clothes should work"
        }
        val rain = if (rainProbability > 45) ", and keep an umbrella close" else ""
        val wind = if (windKmh > 35) ", with a wind-resistant outer layer" else ""
        val uv = if ((maxUv ?: 0.0) >= 6) ". Sunscreen is a good idea." else "."
        return clothing + rain + wind + uv
    }

    fun bestTimeOfDay(hourly: List<HourlyForecast>): String {
        val best = hourly
            .filter { it.time.hour in 7..21 }
            .minByOrNull {
                val comfortPenalty = kotlin.math.abs(it.temperatureC - 21.0).toInt()
                it.precipitationProbability + it.windSpeedKmh.toInt() / 2 + comfortPenalty
            }
        return best?.let { "Best window: ${"%02d:00".format(it.time.hour)}, with lower rain risk and calmer conditions." }
            ?: "Best window is unclear, but the day still has usable forecast guidance."
    }

    fun windWarning(hourly: List<HourlyForecast>): String? {
        val maxWind = hourly.maxOfOrNull { it.windSpeedKmh } ?: return null
        return if (maxWind > 35) "Strong wind may pick up, peaking near ${maxWind.toInt()} km/h." else null
    }

    fun rainWarning(today: DailyForecast?, hourly: List<HourlyForecast>): String? {
        val maxRain = maxOf(today?.precipitationProbability ?: 0, hourly.maxOfOrNull { it.precipitationProbability } ?: 0)
        val eveningRain = hourly.filter { it.time.hour >= 17 }.maxOfOrNull { it.precipitationProbability } ?: 0
        return when {
            maxRain > 60 && eveningRain == maxRain -> "Rain is likely later today. Bring an umbrella before the evening."
            maxRain > 60 -> "Rain is likely today. Keep wet-weather plans ready."
            maxRain in 30..60 -> "Rain is possible, but the signal is not locked in."
            else -> null
        }
    }
}
