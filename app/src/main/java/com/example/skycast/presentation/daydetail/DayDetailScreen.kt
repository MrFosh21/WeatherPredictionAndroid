package com.example.skycast.presentation.daydetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.core.util.DateFormatterHelper
import com.example.skycast.core.util.UnitConverter
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.presentation.components.ErrorView
import com.example.skycast.presentation.components.GlassCard
import com.example.skycast.presentation.components.GradientBackground
import com.example.skycast.presentation.components.HourlyForecastRow
import com.example.skycast.presentation.components.LoadingView
import com.example.skycast.presentation.components.WeatherIcon
import java.time.LocalDate

@Composable
fun DayDetailScreen(state: DayDetailState, onBack: () -> Unit, onRefresh: () -> Unit) {
    val condition = state.day?.condition ?: WeatherCondition.PARTLY_CLOUDY
    GradientBackground(condition, isDay = true) {
        when {
            state.loading -> LoadingView()
            state.error != null -> ErrorView(state.error, onRefresh)
            state.day != null -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 42.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = Color.White) }
                        Text("Day forecast", Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold)
                        IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, "Refresh", tint = Color.White) }
                    }
                }
                item {
                    GlassCard(Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
                            WeatherIcon(condition, true)
                            Column(Modifier.weight(1f)) {
                                val date = LocalDate.parse(state.date)
                                Text(DateFormatterHelper.dayName(date), color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
                                Text(DateFormatterHelper.shortDate(date), color = Color.White.copy(alpha = 0.78f))
                                Text(
                                    "${UnitConverter.temperatureLabel(state.day.highC, state.settings.temperatureUnit)} / ${UnitConverter.temperatureLabel(state.day.lowC, state.settings.temperatureUnit)}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("${state.day.precipitationProbability}% precipitation risk", color = Color.White.copy(alpha = 0.78f))
                            }
                        }
                    }
                }
                item {
                    GlassCard(Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("What to expect", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(state.explanation.orEmpty(), color = Color.White.copy(alpha = 0.84f))
                            Text("Forecast confidence: ${state.day.confidence.score}%", color = Color.White)
                            Text(state.day.confidence.explanation, color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                }
                item { HourlyForecastRow(state.hourly, state.settings) }
            }
        }
    }
}
