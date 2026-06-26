package com.example.skycast.presentation.onboarding

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.presentation.components.GlassCard
import com.example.skycast.presentation.components.GradientBackground
import com.example.skycast.presentation.components.WeatherIcon

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onSearchCity: () -> Unit,
    onContinueDemo: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) onPermissionGranted() else onPermissionDenied()
    }
    GradientBackground(WeatherCondition.CLEAR, isDay = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherIcon(WeatherCondition.PARTLY_CLOUDY, true, Modifier.size(116.dp))
            Spacer(Modifier.height(24.dp))
            Text(
                "Your weather, beautifully predicted.",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 38.sp,
                lineHeight = 42.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "SkyCast uses your location only to fetch local forecasts from Open-Meteo.",
                color = Color.White.copy(alpha = 0.82f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.loading,
                        onClick = {
                            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                        }
                    ) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = null)
                        Text("Use My Location", Modifier.padding(start = 8.dp))
                    }
                    OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = onSearchCity) {
                        Icon(Icons.Rounded.Search, contentDescription = null)
                        Text("Search City Instead", Modifier.padding(start = 8.dp))
                    }
                    OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = onContinueDemo) {
                        Text("Continue with Montreal")
                    }
                    if (state.loading) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CircularProgressIndicator(Modifier.size(20.dp), color = Color.White)
                            Text("Finding your forecast...", color = Color.White)
                        }
                    }
                    state.error?.let { Text(it, color = Color.White.copy(alpha = 0.9f)) }
                    if (state.permissionDenied) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Open App Settings")
                        }
                    }
                    Text("Your location is only used to fetch weather forecasts.", color = Color.White.copy(alpha = 0.68f))
                }
            }
        }
    }
}
