package com.example.skycast.domain.intelligence

import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.ForecastConfidence
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.domain.model.WeatherInsight
import kotlin.math.abs

class WeatherInsightEngine(
    private val adviceEngine: WeatherAdviceEngine = WeatherAdviceEngine(),
    private val confidenceCalculator: ForecastConfidenceCalculator = ForecastConfidenceCalculator()
) {
    fun buildInsight(
        condition: WeatherCondition,
        currentTempC: Double,
        apparentTempC: Double,
        windKmh: Double,
        hourly: List<HourlyForecast>,
        daily: List<DailyForecast>
    ): WeatherInsight {
        val today = daily.firstOrNull()
        val tomorrow = daily.getOrNull(1)
        val rainWarning = adviceEngine.rainWarning(today, hourly)
        val windWarning = adviceEngine.windWarning(hourly)
        val trend = temperatureTrend(daily)
        val feel = if (abs(apparentTempC - currentTempC) >= 3) {
            "It feels closer to ${apparentTempC.toInt()} C"
        } else {
            "It feels close to the measured temperature"
        }
        val summary = buildString {
            append("${condition.displayName} now. ")
            append("$feel. ")
            if (rainWarning != null) append(rainWarning) else append("Rain risk stays manageable in the near term.")
            if (tomorrow != null && today != null) {
                val delta = tomorrow.highC - today.highC
                when {
                    delta <= -5 -> append(" Tomorrow is noticeably cooler.")
                    delta >= 5 -> append(" Tomorrow warms up noticeably.")
                }
            }
        }
        val confidence = confidenceCalculator.confidenceForDay(0, hourly.take(24))
        return WeatherInsight(
            summary = summary,
            trend = trend,
            rainWarning = rainWarning,
            windWarning = windWarning,
            outfitSuggestion = adviceEngine.outfitSuggestion(
                currentTempC = currentTempC,
                maxUv = today?.uvIndex,
                rainProbability = today?.precipitationProbability ?: 0,
                windKmh = windKmh
            ),
            bestTime = adviceEngine.bestTimeOfDay(hourly.take(24)),
            confidence = confidence
        )
    }

    private fun temperatureTrend(daily: List<DailyForecast>): String {
        val next = daily.take(4)
        if (next.size < 3) return "Not enough days yet for a reliable trend."
        val highs = next.map { it.highC }
        val warming = highs.zipWithNext().all { it.second > it.first }
        val cooling = highs.zipWithNext().all { it.second < it.first }
        return when {
            warming -> "The next few days trend warmer."
            cooling -> "The next few days trend cooler."
            highs.maxOrNull() == next.first().highC -> "Today looks like one of the warmer days this stretch."
            else -> "Temperatures hold a fairly steady pattern."
        }
    }

    fun confidenceForDay(dayIndex: Int, hourly: List<HourlyForecast>): ForecastConfidence =
        confidenceCalculator.confidenceForDay(dayIndex, hourly)
}
