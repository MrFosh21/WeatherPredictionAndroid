package com.example.skycast.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.Umbrella
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.core.util.DateFormatterHelper
import com.example.skycast.core.util.UnitConverter
import com.example.skycast.domain.model.CurrentWeather
import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.ForecastConfidence
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.TemperatureUnit
import com.example.skycast.domain.model.TimeFormatPreference
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.domain.model.WeatherForecast
import com.example.skycast.domain.model.WeatherInsight
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun GradientBackground(
    condition: WeatherCondition,
    isDay: Boolean,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "sky")
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(9000), RepeatMode.Reverse),
        label = "drift"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = gradientFor(condition, isDay),
                    start = Offset(0f, 1200f * drift),
                    end = Offset(900f, 200f + 900f * (1f - drift))
                )
            )
    ) {
        Canvas(Modifier.fillMaxSize().alpha(0.22f)) {
            drawCircle(Color.White, radius = size.minDimension * 0.32f, center = Offset(size.width * 0.82f, size.height * 0.12f))
            drawCircle(Color.White, radius = size.minDimension * 0.22f, center = Offset(size.width * 0.15f, size.height * 0.35f))
        }
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.20f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(Modifier.padding(18.dp)) { content() }
    }
}

@Composable
fun WeatherHero(forecast: WeatherForecast, settings: UserSettings) {
    val current = forecast.current
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(forecast.city.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text(DateFormatterHelper.todayLabel(current.time.toLocalDate()), color = Color.White.copy(alpha = 0.78f))
            Spacer(Modifier.height(18.dp))
            Text(
                UnitConverter.temperatureLabel(current.temperatureC, settings.temperatureUnit),
                fontSize = 86.sp,
                lineHeight = 82.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                "${current.condition.displayName} - feels like ${UnitConverter.temperatureLabel(current.apparentTemperatureC, settings.temperatureUnit)}",
                color = Color.White.copy(alpha = 0.86f),
                style = MaterialTheme.typography.titleMedium
            )
            forecast.daily.firstOrNull()?.let {
                Text(
                    "H: ${UnitConverter.temperatureLabel(it.highC, settings.temperatureUnit)}  L: ${UnitConverter.temperatureLabel(it.lowC, settings.temperatureUnit)}",
                    color = Color.White.copy(alpha = 0.76f)
                )
            }
        }
        WeatherIcon(condition = current.condition, isDay = current.isDay, modifier = Modifier.size(112.dp))
    }
}

@Composable
fun SmartInsightCard(insight: WeatherInsight, adviceEnabled: Boolean) {
    GlassCard(Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Smart forecast", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(insight.summary, color = Color.White.copy(alpha = 0.88f))
            Text(insight.trend, color = Color.White.copy(alpha = 0.82f))
            AnimatedVisibility(visible = adviceEnabled) {
                Text(insight.outfitSuggestion, color = Color.White.copy(alpha = 0.82f))
            }
            Text("Forecast confidence: ${insight.confidence.score}%", color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(insight.confidence.explanation, color = Color.White.copy(alpha = 0.72f), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun HourlyForecastRow(hours: List<HourlyForecast>, settings: UserSettings) {
    SectionTitle("Next 24 hours")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
        items(hours.take(24)) { hour ->
            GlassCard(Modifier.width(96.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(DateFormatterHelper.hour(hour.time, settings.timeFormat), color = Color.White.copy(alpha = 0.82f), maxLines = 1)
                    WeatherIcon(hour.condition, isDay = hour.time.hour in 7..19, modifier = Modifier.size(38.dp))
                    Text(UnitConverter.temperatureLabel(hour.temperatureC, settings.temperatureUnit), color = Color.White, fontWeight = FontWeight.Bold)
                    Text("${hour.precipitationProbability}%", color = Color.White.copy(alpha = 0.72f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun DailyForecastList(days: List<DailyForecast>, settings: UserSettings, onDayClick: (DailyForecast) -> Unit) {
    SectionTitle("10 day outlook")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        days.take(10).forEach { day ->
            GlassCard(Modifier.fillMaxWidth(), onClick = { onDayClick(day) }) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    Column(Modifier.width(58.dp)) {
                        Text(DateFormatterHelper.dayName(day.date), color = Color.White, fontWeight = FontWeight.Bold)
                        Text(DateFormatterHelper.shortDate(day.date), color = Color.White.copy(alpha = 0.68f), style = MaterialTheme.typography.bodySmall)
                    }
                    WeatherIcon(day.condition, isDay = true, modifier = Modifier.size(38.dp))
                    Column(Modifier.weight(1f)) {
                        Text(day.condition.displayName, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        TemperatureRangeBar(day.lowC, day.highC)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${UnitConverter.temperatureLabel(day.highC, settings.temperatureUnit)} / ${UnitConverter.temperatureLabel(day.lowC, settings.temperatureUnit)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text("${day.precipitationProbability}% rain", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDetailsGrid(forecast: WeatherForecast, settings: UserSettings) {
    val current = forecast.current
    val today = forecast.daily.firstOrNull()
    SectionTitle("Details")
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            WeatherMetricCard("Humidity", "${current.humidityPercent}%", "Comfort signal", Icons.Rounded.WaterDrop, Modifier.weight(1f))
            WeatherMetricCard("Wind", UnitConverter.windLabel(current.windSpeedKmh, settings.windUnit), windText(current.windSpeedKmh), Icons.Rounded.Air, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            WeatherMetricCard("UV index", today?.uvIndex?.toInt()?.toString() ?: "--", uvText(today?.uvIndex), Icons.Rounded.WbSunny, Modifier.weight(1f))
            WeatherMetricCard("Pressure", current.pressureHpa?.toInt()?.let { "$it hPa" } ?: "--", "Sea-level pressure", Icons.Rounded.Navigation, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            WeatherMetricCard("Clouds", current.cloudCoverPercent?.let { "$it%" } ?: "--", "Sky coverage", Icons.Rounded.Cloud, Modifier.weight(1f))
            WeatherMetricCard("Feels like", UnitConverter.temperatureLabel(current.apparentTemperatureC, settings.temperatureUnit), "Human comfort", Icons.Rounded.DeviceThermostat, Modifier.weight(1f))
        }
    }
}

@Composable
fun WeatherMetricCard(title: String, value: String, caption: String, icon: ImageVector, modifier: Modifier = Modifier) {
    GlassCard(modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = title, tint = Color.White.copy(alpha = 0.86f))
            Text(title, color = Color.White.copy(alpha = 0.76f), style = MaterialTheme.typography.bodySmall)
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(caption, color = Color.White.copy(alpha = 0.68f), style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
    }
}

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GlassCard {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CircularProgressIndicator(color = Color.White)
                Text("Reading the sky...", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        GlassCard(Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Forecast unavailable", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(message, color = Color.White.copy(alpha = 0.8f))
                Button(onClick = onRetry) { Text("Retry") }
            }
        }
    }
}

@Composable
fun WeatherIcon(condition: WeatherCondition, isDay: Boolean, modifier: Modifier = Modifier.size(72.dp)) {
    val transition = rememberInfiniteTransition(label = "icon")
    val pulse by transition.animateFloat(
        0.82f,
        1f,
        infiniteRepeatable(tween(1600), RepeatMode.Reverse),
        label = "pulse"
    )
    val icon = when (condition) {
        WeatherCondition.CLEAR -> if (isDay) Icons.Rounded.WbSunny else Icons.Rounded.NightsStay
        WeatherCondition.PARTLY_CLOUDY, WeatherCondition.CLOUDY, WeatherCondition.FOG -> Icons.Rounded.Cloud
        WeatherCondition.DRIZZLE, WeatherCondition.RAIN, WeatherCondition.HEAVY_RAIN, WeatherCondition.SNOW -> Icons.Rounded.Umbrella
        WeatherCondition.STORM -> Icons.Rounded.Thunderstorm
        WeatherCondition.UNKNOWN -> Icons.Rounded.Visibility
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = condition.displayName, tint = Color.White.copy(alpha = pulse), modifier = Modifier.fillMaxSize().padding(18.dp))
    }
}

@Composable
fun TemperatureRangeBar(low: Double, high: Double) {
    val width = ((high - low).coerceIn(2.0, 18.0) / 18.0).toFloat()
    Box(
        Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(99.dp))
            .background(Color.White.copy(alpha = 0.18f))
    ) {
        Box(
            Modifier
                .fillMaxWidth(width)
                .height(6.dp)
                .clip(RoundedCornerShape(99.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF80D8FF), Color(0xFFFFD166))))
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 10.dp, bottom = 6.dp)
    )
}

private fun gradientFor(condition: WeatherCondition, isDay: Boolean): List<Color> {
    if (!isDay) return listOf(Color(0xFF050B18), Color(0xFF18264A), Color(0xFF3C1B5E))
    return when (condition) {
        WeatherCondition.CLEAR -> listOf(Color(0xFF2EA7FF), Color(0xFFFFC857), Color(0xFFFF7A59))
        WeatherCondition.PARTLY_CLOUDY -> listOf(Color(0xFF6EBBFF), Color(0xFFB8D8F8), Color(0xFFFFD6A3))
        WeatherCondition.CLOUDY, WeatherCondition.FOG -> listOf(Color(0xFF7893B0), Color(0xFFB8C7D6), Color(0xFF5F7FA5))
        WeatherCondition.DRIZZLE, WeatherCondition.RAIN, WeatherCondition.HEAVY_RAIN -> listOf(Color(0xFF102A55), Color(0xFF3657A4), Color(0xFF6D5DF6))
        WeatherCondition.SNOW -> listOf(Color(0xFFC8F1FF), Color(0xFFF7FBFF), Color(0xFF7CA8D8))
        WeatherCondition.STORM -> listOf(Color(0xFF08111F), Color(0xFF202A5A), Color(0xFF6D4DF5))
        WeatherCondition.UNKNOWN -> listOf(Color(0xFF516F94), Color(0xFF8DA7C1))
    }
}

private fun windText(speedKmh: Double): String = when {
    speedKmh > 35 -> "Strong wind"
    speedKmh > 18 -> "Breezy"
    else -> "Light breeze"
}

private fun uvText(uv: Double?): String = when {
    uv == null -> "Not reported"
    uv >= 8 -> "Very high"
    uv >= 6 -> "High, use sunscreen"
    uv >= 3 -> "Moderate"
    else -> "Low"
}

@Preview
@Composable
private fun SmartInsightPreview() {
    GradientBackground(WeatherCondition.CLEAR, true) {
        Column(Modifier.padding(24.dp)) {
            SmartInsightCard(
                WeatherInsight(
                    summary = "Warm afternoon ahead. Low rain risk makes it a good day to be outside.",
                    trend = "The next few days trend warmer.",
                    rainWarning = null,
                    windWarning = null,
                    outfitSuggestion = "Comfortable clothes should work. Sunscreen is a good idea.",
                    bestTime = "Best window: 14:00.",
                    confidence = ForecastConfidence(92, "Stable near-term forecast with low uncertainty.")
                ),
                adviceEnabled = true
            )
        }
    }
}
