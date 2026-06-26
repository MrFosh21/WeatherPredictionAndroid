package com.example.skycast.presentation.search

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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
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
fun CitySearchScreen(
    state: CitySearchState,
    onQueryChange: (String) -> Unit,
    onCitySelected: (City) -> Unit,
    onBack: () -> Unit
) {
    GradientBackground(WeatherCondition.PARTLY_CLOUDY, true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 42.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = Color.White) }
                Text("Search city", color = Color.White, fontWeight = FontWeight.Bold)
            }
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("City, region, or country") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) }
            )
            state.error?.let { Text(it, color = Color.White) }
            if (state.loading) CircularProgressIndicator(color = Color.White)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(state.results, key = { it.id }) { city ->
                    GlassCard(Modifier.fillMaxWidth().clickable { onCitySelected(city) }) {
                        ListItem(
                            headlineContent = { Text(city.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text(city.displayName) },
                            leadingContent = { Icon(Icons.Rounded.LocationCity, contentDescription = null) }
                        )
                    }
                }
            }
        }
    }
}
