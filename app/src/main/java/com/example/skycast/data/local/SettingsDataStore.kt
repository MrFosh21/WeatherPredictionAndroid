package com.example.skycast.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.skycast.domain.model.AppearancePreference
import com.example.skycast.domain.model.TemperatureUnit
import com.example.skycast.domain.model.TimeFormatPreference
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WindUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.skyCastSettingsStore by preferencesDataStore(name = "skycast_settings")

class SettingsDataStore(private val context: Context) {
    val settings: Flow<UserSettings> = context.skyCastSettingsStore.data.map { preferences ->
        UserSettings(
            temperatureUnit = preferences[Keys.TEMPERATURE]?.let(TemperatureUnit::valueOf) ?: TemperatureUnit.CELSIUS,
            windUnit = preferences[Keys.WIND]?.let(WindUnit::valueOf) ?: WindUnit.KMH,
            timeFormat = preferences[Keys.TIME]?.let(TimeFormatPreference::valueOf) ?: TimeFormatPreference.HOUR_24,
            appearance = preferences[Keys.APPEARANCE]?.let(AppearancePreference::valueOf) ?: AppearancePreference.SYSTEM,
            adviceEnabled = preferences[Keys.ADVICE] ?: true
        )
    }

    suspend fun update(settings: UserSettings) {
        context.skyCastSettingsStore.edit { preferences ->
            preferences[Keys.TEMPERATURE] = settings.temperatureUnit.name
            preferences[Keys.WIND] = settings.windUnit.name
            preferences[Keys.TIME] = settings.timeFormat.name
            preferences[Keys.APPEARANCE] = settings.appearance.name
            preferences[Keys.ADVICE] = settings.adviceEnabled
        }
    }

    private object Keys {
        val TEMPERATURE = stringPreferencesKey("temperature_unit")
        val WIND = stringPreferencesKey("wind_unit")
        val TIME = stringPreferencesKey("time_format")
        val APPEARANCE = stringPreferencesKey("appearance")
        val ADVICE = booleanPreferencesKey("advice_enabled")
    }
}
