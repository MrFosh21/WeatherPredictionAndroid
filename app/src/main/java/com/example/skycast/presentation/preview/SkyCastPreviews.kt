package com.example.skycast.presentation.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skycast.core.ui.theme.SkyCastTheme
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.CurrentWeather
import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.ForecastConfidence
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.domain.model.WeatherForecast
import com.example.skycast.domain.model.WeatherInsight
import com.example.skycast.presentation.components.DailyForecastList
import com.example.skycast.presentation.components.ErrorView
import com.example.skycast.presentation.components.GradientBackground
import com.example.skycast.presentation.components.HourlyForecastRow
import com.example.skycast.presentation.components.LoadingView
import com.example.skycast.presentation.components.SmartInsightCard
import com.example.skycast.presentation.components.WeatherDetailsGrid
import com.example.skycast.presentation.components.WeatherMetricCard
import com.example.skycast.presentation.home.HomeScreen
import com.example.skycast.presentation.home.HomeState
import java.time.LocalDate
import java.time.LocalDateTime

@Preview(name = "Home sunny", showBackground = true)
@Composable
fun HomeSunnyPreview() {
    SkyCastTheme {
        HomeScreen(
            state = HomeState(city = previewCity, loading = false, forecast = previewForecast(WeatherCondition.CLEAR, true)),
            onRefresh = {},
            onSearch = {},
            onSaved = {},
            onSettings = {},
            onDayClick = {}
        )
    }
}

@Preview(name = "Home rainy", showBackground = true)
@Composable
fun HomeRainyPreview() {
    SkyCastTheme {
        HomeScreen(
            state = HomeState(city = previewCity, loading = false, forecast = previewForecast(WeatherCondition.RAIN, true)),
            onRefresh = {},
            onSearch = {},
            onSaved = {},
            onSettings = {},
            onDayClick = {}
        )
    }
}

@Preview(name = "Home night", showBackground = true)
@Composable
fun HomeNightPreview() {
    SkyCastTheme {
        HomeScreen(
            state = HomeState(city = previewCity, loading = false, forecast = previewForecast(WeatherCondition.CLEAR, false)),
            onRefresh = {},
            onSearch = {},
            onSaved = {},
            onSettings = {},
            onDayClick = {}
        )
    }
}

@Preview(name = "Hourly card row")
@Composable
fun HourlyRowPreview() {
    GradientBackground(WeatherCondition.PARTLY_CLOUDY, true) {
        Column(Modifier.padding(20.dp)) {
            HourlyForecastRow(previewHours, UserSettings())
        }
    }
}

@Preview(name = "Daily forecast cards")
@Composable
fun DailyForecastPreview() {
    GradientBackground(WeatherCondition.CLOUDY, true) {
        Column(Modifier.padding(20.dp)) {
            DailyForecastList(previewDays, UserSettings(), {})
        }
    }
}

@Preview(name = "Weather details grid")
@Composable
fun DetailsGridPreview() {
    GradientBackground(WeatherCondition.CLEAR, true) {
        Column(Modifier.padding(20.dp)) {
            WeatherDetailsGrid(previewForecast(WeatherCondition.CLEAR, true), UserSettings())
        }
    }
}

@Preview(name = "Loading state")
@Composable
fun LoadingPreview() {
    GradientBackground(WeatherCondition.CLEAR, true) { LoadingView() }
}

@Preview(name = "Error state")
@Composable
fun ErrorPreview() {
    GradientBackground(WeatherCondition.STORM, true) { ErrorView("No internet connection.", {}) }
}

@Preview(name = "Metric card")
@Composable
fun MetricCardPreview() {
    GradientBackground(WeatherCondition.CLEAR, true) {
        Column(Modifier.padding(20.dp)) {
            WeatherMetricCard("UV Index", "6", "High, use sunscreen", Icons.Rounded.WbSunny)
        }
    }
}

private val previewCity = City("preview", "Montreal", "Canada", "Quebec", 45.5, -73.5)

private fun previewForecast(condition: WeatherCondition, isDay: Boolean) = WeatherForecast(
    city = previewCity,
    current = CurrentWeather(
        time = LocalDateTime.of(2026, 6, 25, 12, 0),
        temperatureC = 24.0,
        apparentTemperatureC = 27.0,
        condition = condition,
        windSpeedKmh = 14.0,
        windDirectionDegrees = 180,
        humidityPercent = 64,
        pressureHpa = 1014.0,
        cloudCoverPercent = 35,
        precipitationMm = 0.0,
        isDay = isDay
    ),
    hourly = previewHours,
    daily = previewDays,
    insight = WeatherInsight(
        summary = "Warm afternoon ahead. Light wind and low rain risk make it a good day to be outside.",
        trend = "The next few days trend warmer.",
        rainWarning = null,
        windWarning = null,
        outfitSuggestion = "Comfortable clothes should work. Sunscreen is a good idea.",
        bestTime = "Best window: 14:00.",
        confidence = ForecastConfidence(92, "Stable near-term forecast with low uncertainty.")
    )
)

private val previewHours = (0 until 24).map { index ->
    HourlyForecast(
        time = LocalDateTime.of(2026, 6, 25, index, 0),
        temperatureC = 18.0 + index / 2.0,
        apparentTemperatureC = 19.0 + index / 2.0,
        condition = if (index in 17..20) WeatherCondition.RAIN else WeatherCondition.PARTLY_CLOUDY,
        precipitationProbability = if (index in 17..20) 62 else 12,
        windSpeedKmh = 8.0 + index % 5,
        humidityPercent = 60,
        pressureHpa = 1012.0
    )
}

private val previewDays = (0 until 10).map { index ->
    DailyForecast(
        date = LocalDate.of(2026, 6, 25).plusDays(index.toLong()),
        condition = WeatherCondition.entries[index % WeatherCondition.entries.size],
        highC = 24.0 + index,
        lowC = 15.0 + index / 2.0,
        precipitationProbability = 15 + index * 6,
        uvIndex = 4.0 + index % 4,
        sunrise = LocalDateTime.of(2026, 6, 25, 5, 5).plusDays(index.toLong()),
        sunset = LocalDateTime.of(2026, 6, 25, 20, 47).plusDays(index.toLong()),
        confidence = ForecastConfidence(90 - index * 3, "Useful outlook with normal uncertainty.")
    )
}
