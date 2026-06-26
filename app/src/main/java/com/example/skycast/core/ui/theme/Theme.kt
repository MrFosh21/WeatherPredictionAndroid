package com.example.skycast.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.skycast.domain.model.AppearancePreference

private val DarkColors = darkColorScheme(
    primary = SkyBlue,
    secondary = AuroraGreen,
    tertiary = SunGold,
    background = NightInk,
    surface = NightInk,
    onPrimary = NightInk,
    onSecondary = NightInk,
    onBackground = CloudSilver,
    onSurface = CloudSilver
)

private val LightColors = lightColorScheme(
    primary = RainViolet,
    secondary = SkyBlue,
    tertiary = SunGold,
    background = CloudSilver,
    surface = CloudSilver,
    onPrimary = CloudSilver,
    onSecondary = NightInk,
    onBackground = NightInk,
    onSurface = NightInk
)

@Composable
fun SkyCastTheme(
    appearance: AppearancePreference = AppearancePreference.SYSTEM,
    content: @Composable () -> Unit
) {
    val dark = when (appearance) {
        AppearancePreference.SYSTEM -> isSystemInDarkTheme()
        AppearancePreference.DARK -> true
        AppearancePreference.LIGHT -> false
    }
    val colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (dark) DarkColors else LightColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = SkyCastTypography,
        content = content
    )
}
