package com.example.skycast.domain.intelligence

import com.example.skycast.domain.model.ForecastConfidence
import com.example.skycast.domain.model.HourlyForecast
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

class ForecastConfidenceCalculator {
    fun confidenceForDay(dayIndex: Int, hourly: List<HourlyForecast>): ForecastConfidence {
        var score = when (dayIndex) {
            0 -> 94
            1 -> 86
            2, 3 -> 76
            else -> 66 - max(0, dayIndex - 4) * 2
        }

        if (hourly.isNotEmpty()) {
            val temperatures = hourly.map { it.temperatureC }
            val swing = (temperatures.maxOrNull() ?: 0.0) - (temperatures.minOrNull() ?: 0.0)
            val uncertainRainHours = hourly.count { it.precipitationProbability in 40..60 }
            val conditionChanges = hourly.zipWithNext().count { it.first.condition != it.second.condition }

            score -= (swing / 8).roundToInt().coerceAtMost(8)
            score -= uncertainRainHours.coerceAtMost(10)
            score -= conditionChanges.coerceAtMost(8)
        }

        val bounded = score.coerceIn(55, 96)
        val explanation = when {
            bounded >= 88 -> "Stable near-term forecast with low uncertainty."
            bounded >= 75 -> "Good signal, with normal day-ahead variation."
            bounded >= 65 -> "Useful outlook, but details can shift."
            else -> "Longer-range pattern with higher uncertainty."
        }
        return ForecastConfidence(bounded, explanation)
    }

    fun confidenceForForecast(dayIndex: Int, dailySpreadC: Double, rainProbability: Int): ForecastConfidence {
        val hourlyPenalty = if (rainProbability in 40..60) 7 else 0
        val spreadPenalty = abs(dailySpreadC).roundToInt().coerceAtMost(10)
        val base = when (dayIndex) {
            0 -> 94
            1 -> 86
            2, 3 -> 76
            else -> 68
        }
        val score = (base - hourlyPenalty - spreadPenalty).coerceIn(55, 96)
        return ForecastConfidence(score, if (score >= 80) "Stable pattern, low uncertainty." else "Pattern may shift as the day gets closer.")
    }
}
