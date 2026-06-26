package com.example.skycast.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingDto(
    val results: List<GeocodingResultDto> = emptyList()
)

@Serializable
data class GeocodingResultDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @SerialName("admin1") val region: String? = null
)
