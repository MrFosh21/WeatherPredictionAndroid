package com.example.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.skycast.core.ui.theme.SkyCastTheme
import com.example.skycast.domain.model.UserSettings
import com.example.skycast.navigation.AppNavGraph
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as SkyCastApplication).container
        val settingsFlow = container.settingsRepository.settings.stateIn(
            scope = lifecycleScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSettings()
        )
        setContent {
            val settings by settingsFlow.collectAsState()
            SkyCastTheme(settings.appearance) {
                AppNavGraph(
                    navController = rememberNavController(),
                    container = container
                )
            }
        }
    }
}
