package com.example.skycast.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.domain.model.City
import com.example.skycast.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SavedLocationsState(
    val cities: List<City> = emptyList()
)

class SavedLocationsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val state: StateFlow<SavedLocationsState> = settingsRepository.savedCities
        .map { SavedLocationsState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SavedLocationsState())

    fun delete(city: City) {
        viewModelScope.launch { settingsRepository.deleteCity(city) }
    }

    companion object {
        fun factory(settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    SavedLocationsViewModel(settingsRepository) as T
            }
    }
}
