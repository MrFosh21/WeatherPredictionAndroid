package com.example.skycast.core.util

import com.example.skycast.domain.model.TemperatureUnit
import com.example.skycast.domain.model.WindUnit
import kotlin.math.roundToInt

object UnitConverter {
    fun temperature(valueCelsius: Double, unit: TemperatureUnit): Int =
        when (unit) {
            TemperatureUnit.CELSIUS -> valueCelsius.roundToInt()
            TemperatureUnit.FAHRENHEIT -> ((valueCelsius * 9 / 5) + 32).roundToInt()
        }

    fun temperatureLabel(valueCelsius: Double, unit: TemperatureUnit): String =
        "${temperature(valueCelsius, unit)}${unit.symbol}"

    fun wind(valueKmh: Double, unit: WindUnit): Int =
        when (unit) {
            WindUnit.KMH -> valueKmh.roundToInt()
            WindUnit.MPH -> (valueKmh * 0.621371).roundToInt()
        }

    fun windLabel(valueKmh: Double, unit: WindUnit): String =
        "${wind(valueKmh, unit)} ${unit.label}"
}
