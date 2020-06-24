package com.chococard.carwash.util.extension

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

fun GoogleMap?.animateCamera(latLng: LatLng = LatLng(13.7244416, 100.3529005), zoom: Float = 14F) {
    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
    this?.animateCamera(cameraUpdate)
}
