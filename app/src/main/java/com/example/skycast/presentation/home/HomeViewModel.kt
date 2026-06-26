package com.example.skycast.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.model.WeatherForecast
import com.example.skycast.domain.repository.SettingsRepository
import com.example.skycast.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val city: City,
    val settings: UserSettings = UserSettings(),
    val loading: Boolean = true,
    val forecast: WeatherForecast? = null,
    val fromCache: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val city: City,
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState(city = city))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { it.copy(settings = settings) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val settings = settingsRepository.settings.first()
            _state.update { it.copy(loading = true, error = null, settings = settings) }
            when (val result = weatherRepository.forecast(city, settings)) {
                is Resource.Success -> _state.update {
                    it.copy(loading = false, forecast = result.data, fromCache = result.fromCache, error = null)
                }
                is Resource.Error -> _state.update { it.copy(loading = false, error = result.message) }
                Resource.Loading -> Unit
            }
        }
    }

    companion object {
        fun factory(city: City, weatherRepository: WeatherRepository, settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    HomeViewModel(city, weatherRepository, settingsRepository) as T
            }
    }
}
