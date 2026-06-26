package com.example.skycast.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.skycast.AppContainer
import com.example.skycast.domain.model.City
import com.example.skycast.presentation.daydetail.DayDetailScreen
import com.example.skycast.presentation.daydetail.DayDetailViewModel
import com.example.skycast.presentation.home.HomeScreen
import com.example.skycast.presentation.home.HomeViewModel
import com.example.skycast.presentation.onboarding.OnboardingScreen
import com.example.skycast.presentation.onboarding.OnboardingViewModel
import com.example.skycast.presentation.saved.SavedLocationsScreen
import com.example.skycast.presentation.saved.SavedLocationsViewModel
import com.example.skycast.presentation.search.CitySearchScreen
import com.example.skycast.presentation.search.CitySearchViewModel
import com.example.skycast.presentation.settings.SettingsScreen
import com.example.skycast.presentation.settings.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    container: AppContainer
) {
    val defaultCity = City("6077243", "Montreal", "Canada", "Quebec", 45.5088, -73.5878)
    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
        composable(Screen.Onboarding.route) {
            val vm: OnboardingViewModel = viewModel(factory = OnboardingViewModel.factory(container.locationRepository))
            val state by vm.state.collectAsState()
            OnboardingScreen(
                state = state,
                onPermissionGranted = { vm.loadCurrentLocation { navController.navigate(Screen.Home.create(it)) } },
                onPermissionDenied = vm::markPermissionDenied,
                onSearchCity = { navController.navigate(Screen.Search.route) },
                onContinueDemo = { navController.navigate(Screen.Home.create(defaultCity)) }
            )
        }
        composable(Screen.Search.route) {
            val vm: CitySearchViewModel = viewModel(factory = CitySearchViewModel.factory(container.weatherRepository, container.settingsRepository))
            val state by vm.state.collectAsState()
            CitySearchScreen(
                state = state,
                onQueryChange = vm::updateQuery,
                onCitySelected = { city ->
                    vm.save(city)
                    navController.navigate(Screen.Home.create(city))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Saved.route) {
            val vm: SavedLocationsViewModel = viewModel(factory = SavedLocationsViewModel.factory(container.settingsRepository))
            val state by vm.state.collectAsState()
            SavedLocationsScreen(
                state = state,
                onDelete = vm::delete,
                onSelect = { navController.navigate(Screen.Home.create(it)) },
                onAdd = { navController.navigate(Screen.Search.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(container.settingsRepository))
            val state by vm.state.collectAsState()
            SettingsScreen(
                state = state,
                onSettingsChange = vm::update,
                onClearCities = vm::clearCities,
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Home.route,
            arguments = cityArgs()
        ) { entry ->
            val city = entry.cityArg()
            val vm: HomeViewModel = viewModel(factory = HomeViewModel.factory(city, container.weatherRepository, container.settingsRepository))
            val state by vm.state.collectAsState()
            HomeScreen(
                state = state,
                onRefresh = vm::refresh,
                onSearch = { navController.navigate(Screen.Search.route) },
                onSaved = { navController.navigate(Screen.Saved.route) },
                onSettings = { navController.navigate(Screen.Settings.route) },
                onDayClick = { navController.navigate(Screen.DayDetail.create(city, it.date.toString())) }
            )
        }
        composable(
            route = Screen.DayDetail.route,
            arguments = cityArgs() + navArgument("date") { type = NavType.StringType }
        ) { entry ->
            val city = entry.cityArg()
            val date = entry.arguments?.getString("date").orEmpty()
            val vm: DayDetailViewModel = viewModel(factory = DayDetailViewModel.factory(city, date, container.weatherRepository, container.settingsRepository))
            val state by vm.state.collectAsState()
            DayDetailScreen(state = state, onBack = { navController.popBackStack() }, onRefresh = vm::refresh)
        }
    }
}

private fun cityArgs() = listOf(
    navArgument("id") { type = NavType.StringType },
    navArgument("name") { type = NavType.StringType },
    navArgument("country") { type = NavType.StringType },
    navArgument("region") { type = NavType.StringType },
    navArgument("lat") { type = NavType.StringType },
    navArgument("lon") { type = NavType.StringType },
    navArgument("current") { type = NavType.BoolType }
)

private fun NavBackStackEntry.cityArg(): City {
    val args = requireNotNull(arguments)
    return City(
        id = args.getString("id").orEmpty(),
        name = args.getString("name").orEmpty(),
        country = args.getString("country").orEmpty(),
        region = args.getString("region").orEmpty().ifBlank { null },
        latitude = args.getString("lat").orEmpty().toDoubleOrNull() ?: 0.0,
        longitude = args.getString("lon").orEmpty().toDoubleOrNull() ?: 0.0,
        isCurrentLocation = args.getBoolean("current")
    )
}
