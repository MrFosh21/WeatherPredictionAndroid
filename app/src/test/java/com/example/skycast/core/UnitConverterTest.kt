package com.example.skycast.core

import com.example.skycast.core.util.UnitConverter
import com.example.skycast.domain.model.TemperatureUnit
import com.example.skycast.domain.model.WindUnit
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {
    @Test
    fun convertsTemperatureUnits() {
        assertEquals(0, UnitConverter.temperature(0.0, TemperatureUnit.CELSIUS))
        assertEquals(32, UnitConverter.temperature(0.0, TemperatureUnit.FAHRENHEIT))
        assertEquals(77, UnitConverter.temperature(25.0, TemperatureUnit.FAHRENHEIT))
    }

    @Test
    fun convertsWindUnits() {
        assertEquals(16, UnitConverter.wind(16.2, WindUnit.KMH))
        assertEquals(10, UnitConverter.wind(16.0934, WindUnit.MPH))
    }
}
