package com.example.skycast.data.mapper

import com.example.skycast.data.remote.dto.GeocodingResultDto
import com.example.skycast.domain.model.City

object CityMapper {
    fun fromDto(dto: GeocodingResultDto): City = City(
        id = dto.id.toString(),
        name = dto.name,
        country = dto.country.orEmpty(),
        region = dto.region,
        latitude = dto.latitude,
        longitude = dto.longitude
    )
}
