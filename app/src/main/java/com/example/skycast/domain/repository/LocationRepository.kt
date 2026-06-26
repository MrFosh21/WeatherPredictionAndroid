package com.example.skycast.domain.repository

import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City

interface LocationRepository {
    suspend fun currentLocationCity(): Resource<City>
}
