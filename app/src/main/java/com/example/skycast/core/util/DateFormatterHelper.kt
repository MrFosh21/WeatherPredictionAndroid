package com.example.skycast.core.util

import com.example.skycast.domain.model.TimeFormatPreference
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateFormatterHelper {
    private val fullDate = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())
    private val shortDate = DateTimeFormatter.ofPattern("MMM d", Locale.getDefault())
    private val hour24 = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val hour12 = DateTimeFormatter.ofPattern("h a", Locale.getDefault())

    fun todayLabel(date: LocalDate = LocalDate.now()): String = date.format(fullDate)

    fun dayName(date: LocalDate): String =
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    fun shortDate(date: LocalDate): String = date.format(shortDate)

    fun hour(time: LocalDateTime, format: TimeFormatPreference): String =
        time.format(if (format == TimeFormatPreference.HOUR_24) hour24 else hour12)

    fun parseDate(value: String): LocalDate = LocalDate.parse(value)

    fun parseDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)
}
