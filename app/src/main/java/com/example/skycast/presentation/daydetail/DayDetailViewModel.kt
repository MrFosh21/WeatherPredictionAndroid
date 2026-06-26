package com.example.skycast.presentation.daydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.DailyForecast
import com.example.skycast.domain.model.HourlyForecast
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.repository.SettingsRepository
import com.example.skycast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class DayDetailState(
    val city: City,
    val date: String,
    val settings: UserSettings = UserSettings(),
    val loading: Boolean = true,
    val day: DailyForecast? = null,
    val hourly: List<HourlyForecast> = emptyList(),
    val explanation: String? = null,
    val error: String? = null
)

class DayDetailViewModel(
    private val city: City,
    private val date: String,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DayDetailState(city = city, date = date))
    val state: StateFlow<DayDetailState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            _state.update { it.copy(loading = true, settings = settings, error = null) }
            when (val result = weatherRepository.forecast(city, settings)) {
                is Resource.Success -> {
                    val target = LocalDate.parse(date)
                    val forecast = result.data
                    val day = forecast.daily.firstOrNull { it.date == target }
                    val hours = forecast.hourly.filter { it.time.toLocalDate() == target }
                    _state.update {
                        it.copy(
                            loading = false,
                            day = day,
                            hourly = hours,
                            explanation = day?.let { selected ->
                                "${selected.condition.displayName} with a high near ${selected.highC.toInt()} C and ${selected.precipitationProbability}% precipitation risk. ${forecast.insight.trend}"
                            },
                            error = if (day == null) "This day is not in the current forecast window." else null
                        )
                    }
                }
                is Resource.Error -> _state.update { it.copy(loading = false, error = result.message) }
                Resource.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory(city: City, date: String, weatherRepository: WeatherRepository, settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    DayDetailViewModel(city, date, weatherRepository, settingsRepository) as T
            }
    }
}
