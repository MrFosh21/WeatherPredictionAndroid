# SkyCast - WeatherPredictionAndroid

SkyCast is a native Android weather companion built with Kotlin, Jetpack Compose, Material 3, MVVM, DataStore, Retrofit, and Open-Meteo.

## What it does

- Loads real current, hourly, and 10 day forecast data from Open-Meteo.
- Supports current-location forecasts with Android runtime location permissions.
- Supports manual city search through Open-Meteo Geocoding.
- Saves recent cities locally with DataStore.
- Stores user settings for temperature units, wind units, time format, appearance, and advice cards.
- Caches the last successful forecast and falls back to it when the network fails.
- Generates rule-based weather intelligence: daily summaries, rain/wind warnings, outfit advice, best time of day, trend text, and forecast confidence.
- Uses a premium Compose UI with animated weather gradients, glass-style cards, weather icons, hourly forecast rows, daily forecast cards, detail grids, onboarding, search, saved locations, settings, and day detail screens.

## Requirements

- Android Studio with JDK 17 or newer.
- Android SDK Platform 36.
- Minimum supported Android version: Android 8.0, API 26.

## Run

Open the project in Android Studio and run the `app` configuration.

From a terminal with `JAVA_HOME` set to JDK 17+ and `ANDROID_HOME` pointing at an Android SDK:

```powershell
.\gradlew.bat test assembleDebug
```

The debug APK is produced at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Notes

SkyCast does not fake prediction data. Forecasts come from Open-Meteo, while the "smart forecast" language and confidence scores are generated locally from rule-based logic.
