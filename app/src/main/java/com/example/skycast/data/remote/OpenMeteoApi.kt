package com.example.skycast.data.remote

import com.example.skycast.data.remote.dto.GeocodingDto
import com.example.skycast.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun forecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 10,
        @Query("current") current: String = CURRENT_FIELDS,
        @Query("hourly") hourly: String = HOURLY_FIELDS,
        @Query("daily") daily: String = DAILY_FIELDS,
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("precipitation_unit") precipitationUnit: String = "mm"
    ): WeatherDto

    @GET("v1/search")
    suspend fun searchCities(
        @Query("name") query: String,
        @Query("count") count: Int = 10,
        @Query("language") language: String = "en",
        @Query("format") format: String = "json"
    ): GeocodingDto

    companion object {
        const val CURRENT_FIELDS = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,cloud_cover,pressure_msl,wind_speed_10m,wind_direction_10m"
        const val HOURLY_FIELDS = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,weather_code,pressure_msl,wind_speed_10m"
        const val DAILY_FIELDS = "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max,uv_index_max"
    }
}
