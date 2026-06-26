package com.example.skycast.domain.model

enum class WeatherCondition(
    val displayName: String,
    val shortDescription: String
) {
    CLEAR("Clear", "Bright and calm"),
    PARTLY_CLOUDY("Partly cloudy", "Sun with soft cloud cover"),
    CLOUDY("Cloudy", "Muted light and fuller cloud cover"),
    FOG("Fog", "Low visibility in the air"),
    DRIZZLE("Drizzle", "Light moisture is possible"),
    RAIN("Rain", "Wet weather moving through"),
    HEAVY_RAIN("Heavy rain", "Strong rain risk"),
    SNOW("Snow", "Cold precipitation expected"),
    STORM("Storm", "Unsettled and electric"),
    UNKNOWN("Weather", "Forecast data available");

    companion object {
        fun fromOpenMeteoCode(code: Int?): WeatherCondition = when (code) {
            0 -> CLEAR
            1, 2 -> PARTLY_CLOUDY
            3 -> CLOUDY
            45, 48 -> FOG
            51, 53, 55, 56, 57 -> DRIZZLE
            61, 63, 66, 67, 80, 81 -> RAIN
            65, 82 -> HEAVY_RAIN
            71, 73, 75, 77, 85, 86 -> SNOW
            95, 96, 99 -> STORM
            else -> UNKNOWN
        }
    }
}
