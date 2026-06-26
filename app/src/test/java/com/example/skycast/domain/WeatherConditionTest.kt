package com.example.skycast.domain

import com.example.skycast.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherConditionTest {
    @Test
    fun mapsOpenMeteoCodes() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromOpenMeteoCode(0))
        assertEquals(WeatherCondition.PARTLY_CLOUDY, WeatherCondition.fromOpenMeteoCode(2))
        assertEquals(WeatherCondition.CLOUDY, WeatherCondition.fromOpenMeteoCode(3))
        assertEquals(WeatherCondition.FOG, WeatherCondition.fromOpenMeteoCode(45))
        assertEquals(WeatherCondition.DRIZZLE, WeatherCondition.fromOpenMeteoCode(53))
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromOpenMeteoCode(61))
        assertEquals(WeatherCondition.HEAVY_RAIN, WeatherCondition.fromOpenMeteoCode(82))
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromOpenMeteoCode(75))
        assertEquals(WeatherCondition.STORM, WeatherCondition.fromOpenMeteoCode(96))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromOpenMeteoCode(999))
    }
}
