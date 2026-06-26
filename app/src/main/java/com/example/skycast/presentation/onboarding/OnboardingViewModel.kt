package com.example.skycast.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingState(
    val loading: Boolean = false,
    val error: String? = null,
    val permissionDenied: Boolean = false
)

class OnboardingViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun loadCurrentLocation(onCityReady: (City) -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, permissionDenied = false) }
            when (val result = locationRepository.currentLocationCity()) {
                is Resource.Success -> onCityReady(result.data)
                is Resource.Error -> _state.update { it.copy(loading = false, error = result.message) }
                Resource.Loading -> Unit
            }
        }
    }

    fun markPermissionDenied() {
        _state.update { it.copy(permissionDenied = true, loading = false, error = "Permission denied. City search still works.") }
    }

    companion object {
        fun factory(locationRepository: LocationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    OnboardingViewModel(locationRepository) as T
            }
    }
}
