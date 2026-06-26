package com.example.skycast.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.presentation.components.DailyForecastList
import com.example.skycast.presentation.components.ErrorView
import com.example.skycast.presentation.components.GradientBackground
import com.example.skycast.presentation.components.HourlyForecastRow
import com.example.skycast.presentation.components.LoadingView
import com.example.skycast.presentation.components.SmartInsightCard
import com.example.skycast.presentation.components.WeatherDetailsGrid
import com.example.skycast.presentation.components.WeatherHero

@Composable
fun HomeScreen(
    state: HomeState,
    onRefresh: () -> Unit,
    onSearch: () -> Unit,
    onSaved: () -> Unit,
    onSettings: () -> Unit,
    onDayClick: (DailyForecast) -> Unit
) {
    val condition = state.forecast?.current?.condition ?: WeatherCondition.CLEAR
    val isDay = state.forecast?.current?.isDay ?: true
    GradientBackground(condition = condition, isDay = isDay) {
        when {
            state.loading && state.forecast == null -> LoadingView()
            state.error != null && state.forecast == null -> ErrorView(state.error, onRefresh)
            state.forecast != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 42.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("SkyCast", color = Color.White.copy(alpha = 0.72f), fontWeight = FontWeight.Bold)
                                if (state.fromCache) Text("Showing last updated forecast", color = Color.White.copy(alpha = 0.82f))
                            }
                            IconButton(onClick = onRefresh) { Icon(Icons.Rounded.Refresh, "Refresh", tint = Color.White) }
                            IconButton(onClick = onSearch) { Icon(Icons.Rounded.Search, "Search", tint = Color.White) }
                            IconButton(onClick = onSaved) { Icon(Icons.Rounded.Bookmark, "Saved locations", tint = Color.White) }
                            IconButton(onClick = onSettings) { Icon(Icons.Rounded.Settings, "Settings", tint = Color.White) }
                        }
                    }
                    item { WeatherHero(state.forecast, state.settings) }
                    item { SmartInsightCard(state.forecast.insight, state.settings.adviceEnabled) }
                    item { HourlyForecastRow(state.forecast.hourly, state.settings) }
                    item { DailyForecastList(state.forecast.daily, state.settings, onDayClick) }
                    item { WeatherDetailsGrid(state.forecast, state.settings) }
                    item {
                        Spacer(Modifier.height(6.dp))
                        Text("Updated ${state.forecast.updatedAt.toLocalTime().withSecond(0).withNano(0)}", color = Color.White.copy(alpha = 0.62f))
                    }
                }
            }
        }
    }
}
