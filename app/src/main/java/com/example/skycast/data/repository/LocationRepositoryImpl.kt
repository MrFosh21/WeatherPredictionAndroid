package com.example.skycast.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.skycast.core.result.Resource
import com.example.skycast.domain.model.City
import com.example.skycast.domain.repository.LocationRepository
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl(private val context: Context) : LocationRepository {
    override suspend fun currentLocationCity(): Resource<City> {
        if (!hasLocationPermission()) {
            return Resource.Error("Location permission is needed for local weather.")
        }
        val playServicesLocation = lastFusedLocation()
        val location = playServicesLocation ?: lastManagerLocation()
        return if (location != null) {
            Resource.Success(
                City(
                    id = "current-location",
                    name = "Current location",
                    country = "",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    isCurrentLocation = true
                )
            )
        } else {
            Resource.Error("Could not read your location. Try searching for a city.")
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    private suspend fun lastFusedLocation(): Location? = suspendCancellableCoroutine { continuation ->
        runCatching {
            LocationServices.getFusedLocationProviderClient(context).lastLocation
                .addOnSuccessListener { continuation.resume(it) }
                .addOnFailureListener { continuation.resume(null) }
                .addOnCanceledListener { continuation.resume(null) }
        }.onFailure {
            continuation.resume(null)
        }
    }

    private fun lastManagerLocation(): Location? = runCatching {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
            .firstNotNullOfOrNull { provider ->
                if (manager.isProviderEnabled(provider)) manager.getLastKnownLocation(provider) else null
            }
    }.getOrNull()
}
