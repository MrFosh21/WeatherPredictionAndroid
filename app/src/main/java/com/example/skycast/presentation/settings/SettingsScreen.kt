package com.example.skycast.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skycast.domain.model.AppearancePreference
import com.example.skycast.domain.model.TemperatureUnit
import com.example.skycast.domain.model.TimeFormatPreference
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.domain.model.WindUnit
import com.example.skycast.presentation.components.GlassCard
import com.example.skycast.presentation.components.GradientBackground

@Composable
fun SettingsScreen(
    state: SettingsState,
    onSettingsChange: (UserSettings) -> Unit,
    onClearCities: () -> Unit,
    onBack: () -> Unit
) {
    GradientBackground(WeatherCondition.CLEAR, false) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 42.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = Color.White) }
                    Text("Settings", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            item {
                SettingGroup("Temperature") {
                    ChipRow(TemperatureUnit.entries.map { it.name }, state.settings.temperatureUnit.name) {
                        onSettingsChange(state.settings.copy(temperatureUnit = TemperatureUnit.valueOf(it)))
                    }
                }
            }
            item {
                SettingGroup("Wind") {
                    ChipRow(WindUnit.entries.map { it.name }, state.settings.windUnit.name) {
                        onSettingsChange(state.settings.copy(windUnit = WindUnit.valueOf(it)))
                    }
                }
            }
            item {
                SettingGroup("Time") {
                    ChipRow(listOf("HOUR_24", "HOUR_12"), state.settings.timeFormat.name) {
                        onSettingsChange(state.settings.copy(timeFormat = TimeFormatPreference.valueOf(it)))
                    }
                }
            }
            item {
                SettingGroup("Appearance") {
                    ChipRow(AppearancePreference.entries.map { it.name }, state.settings.appearance.name) {
                        onSettingsChange(state.settings.copy(appearance = AppearancePreference.valueOf(it)))
                    }
                }
            }
            item {
                GlassCard(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Weather advice", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Show outfit and activity suggestions", color = Color.White.copy(alpha = 0.7f))
                        }
                        Switch(
                            checked = state.settings.adviceEnabled,
                            onCheckedChange = { onSettingsChange(state.settings.copy(adviceEnabled = it)) }
                        )
                    }
                }
            }
            item {
                GlassCard(Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("About SkyCast", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Real forecasts from Open-Meteo, interpreted with local rule-based weather intelligence.", color = Color.White.copy(alpha = 0.76f))
                        Button(onClick = onClearCities) {
                            Icon(Icons.Rounded.DeleteSweep, contentDescription = null)
                            Text("Clear saved locations", Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingGroup(title: String, content: @Composable () -> Unit) {
    GlassCard(Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun ChipRow(values: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        values.forEach { value ->
            FilterChip(
                selected = selected == value,
                onClick = { onSelect(value) },
                label = { Text(value.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
