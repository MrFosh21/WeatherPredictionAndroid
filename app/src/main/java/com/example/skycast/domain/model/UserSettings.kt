package com.example.skycast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
    val windUnit: WindUnit = WindUnit.KMH,
    val timeFormat: TimeFormatPreference = TimeFormatPreference.HOUR_24,
    val appearance: AppearancePreference = AppearancePreference.SYSTEM,
    val adviceEnabled: Boolean = true
)

enum class TemperatureUnit(val symbol: String) {
    CELSIUS("C"),
    FAHRENHEIT("F")
}

enum class WindUnit(val label: String) {
    KMH("km/h"),
    MPH("mph")
}

enum class TimeFormatPreference {
    HOUR_12,
    HOUR_24
}

enum class AppearancePreference {
    SYSTEM,
    LIGHT,
    DARK
}
