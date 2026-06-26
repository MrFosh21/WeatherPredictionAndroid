package com.example.skycast.navigation

import android.net.Uri
import com.example.skycast.domain.model.City

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Search : Screen("search")
    data object Saved : Screen("saved")
    data object Settings : Screen("settings")
    data object Home : Screen("home/{id}/{name}/{country}/{region}/{lat}/{lon}/{current}") {
        fun create(city: City): String = listOf(
            city.id,
            city.name,
            city.country,
            city.region.orEmpty(),
            city.latitude.toString(),
            city.longitude.toString(),
            city.isCurrentLocation.toString()
        ).joinToString(prefix = "home/", separator = "/") { Uri.encode(it) }
    }
    data object DayDetail : Screen("day/{id}/{name}/{country}/{region}/{lat}/{lon}/{current}/{date}") {
        fun create(city: City, date: String): String = Screen.Home.create(city).replace("home/", "day/") + "/${Uri.encode(date)}"
    }
}
