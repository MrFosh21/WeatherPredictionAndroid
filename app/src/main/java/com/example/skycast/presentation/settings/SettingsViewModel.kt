package com.example.skycast.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsState(
    val settings: UserSettings = UserSettings()
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val state: StateFlow<SettingsState> = settingsRepository.settings
        .map { SettingsState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsState())

    fun update(settings: UserSettings) {
        viewModelScope.launch { settingsRepository.updateSettings(settings) }
    }

    fun clearCities() {
        viewModelScope.launch { settingsRepository.clearCities() }
    }

    companion object {
        fun factory(settingsRepository: SettingsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    SettingsViewModel(settingsRepository) as T
            }
    }
}
