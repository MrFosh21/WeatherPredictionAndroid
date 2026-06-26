package com.example.skycast.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: String,
    val name: String,
    val country: String,
    val region: String? = null,
    val latitude: Double,
    val longitude: Double,
    val isCurrentLocation: Boolean = false
) {
    val displayName: String
        get() = listOfNotNull(name, region, country).filter { it.isNotBlank() }.joinToString(", ")
}
