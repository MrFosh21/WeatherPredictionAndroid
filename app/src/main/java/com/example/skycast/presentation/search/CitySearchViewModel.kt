package com.example.skycast.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.repository.SettingsRepository
import com.example.skycast.domain.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CitySearchState(
    val query: String = "",
    val loading: Boolean = false,
    val results: List<City> = emptyList(),
    val error: String? = null
)

class CitySearchViewModel(
    private val weatherRepository: WeatherRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CitySearchState())
    val state: StateFlow<CitySearchState> = _state.asStateFlow()
    private var searchJob: Job? = null

    fun updateQuery(query: String) {
        _state.update { it.copy(query = query, error = null) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            if (query.length < 2) {
                _state.update { it.copy(results = emptyList(), loading = false) }
                return@launch
            }
            _state.update { it.copy(loading = true) }
            when (val result = weatherRepository.searchCities(query)) {
                is Resource.Success -> _state.update { it.copy(loading = false, results = result.data) }
                is Resource.Error -> _state.update { it.copy(loading = false, error = result.message) }
                Resource.Loading -> Unit
            }
        }
    }

    fun save(city: City) {
        viewModelScope.launch { settingsRepository.saveCity(city) }
    }

    companion object {
        fun factory(weatherRepository: WeatherRepository, settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    CitySearchViewModel(weatherRepository, settingsRepository) as T
            }
    }
}
