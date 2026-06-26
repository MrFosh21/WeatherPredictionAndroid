package com.example.skycast.presentation.saved

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skycast.domain.model.City
import com.example.skycast.domain.model.WeatherCondition
import com.example.skycast.presentation.components.GlassCard
import com.example.skycast.presentation.components.GradientBackground

@Composable
fun SavedLocationsScreen(
    state: SavedLocationsState,
    onDelete: (City) -> Unit,
    onSelect: (City) -> Unit,
    onAdd: () -> Unit,
    onBack: () -> Unit
) {
    GradientBackground(WeatherCondition.CLOUDY, true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 42.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = Color.White) }
                Text("Saved locations", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                FloatingActionButton(onClick = onAdd) { Icon(Icons.Rounded.Add, "Add city") }
            }
            if (state.cities.isEmpty()) {
                GlassCard(Modifier.fillMaxWidth()) {
                    Text("Search for a city to save it here.", color = Color.White)
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.cities, key = { it.id }) { city ->
                    GlassCard(Modifier.fillMaxWidth().clickable { onSelect(city) }) {
                        ListItem(
                            headlineContent = { Text(city.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(city.displayName) },
                            leadingContent = { Icon(Icons.Rounded.Place, contentDescription = null) },
                            trailingContent = {
                                IconButton(onClick = { onDelete(city) }) {
                                    Icon(Icons.Rounded.Delete, "Delete")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
