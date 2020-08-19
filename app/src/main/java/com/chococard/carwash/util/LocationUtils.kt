package com.chococard.carwash.util

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun createLocationRequest(): LocationRequest = LocationRequest.create().apply {
    interval = 20_000
    fastestInterval = 15_000
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
}

fun Location.asString(format: Int = Location.FORMAT_DEGREES): String {
    val latitude = Location.convert(latitude, format)
    val longitude = Location.convert(longitude, format)
    return "Location is: $latitude, $longitude"
}

suspend fun FusedLocationProviderClient.awaitLastLocation(): Location =
    suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location)
        }.addOnFailureListener { e ->
            continuation.resumeWithException(e)
        }
    }

fun FusedLocationProviderClient.locationFlow() = callbackFlow<Location> {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result ?: return
            for (location in result.locations) {
                offer(location) // emit location into the Flow using ProducerScope.offer
            }
        }
    }

    requestLocationUpdates(
        createLocationRequest(),
        callback,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e) // in case of exception, close the Flow
    }

    awaitClose {
        removeLocationUpdates(callback) // clean up when Flow collection ends
    }
}
