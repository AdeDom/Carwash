package com.chococard.carwash.ui.map

import android.content.Context
import com.chococard.carwash.data.models.EmployeeLocation
import com.chococard.carwash.util.extension.setEmployeeLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Employee(
    context: Context,
    googleMap: GoogleMap?,
    employeeLocation: List<EmployeeLocation>
) {

    init {
        MapFragment.sMarkerEmployee.forEach {
            it.remove()
        }
        MapFragment.sMarkerEmployee.clear()

        employeeLocation.forEach { employee ->
            val (_, _, _, latitude, longitude) = employee
            if (latitude != null && longitude != null) {
                val marker = googleMap?.addMarker(MarkerOptions().apply {
                    position(LatLng(latitude, longitude))
                    icon(BitmapDescriptorFactory.fromBitmap(context.setEmployeeLocation()))
                })
                marker?.let { MapFragment.sMarkerEmployee.add(it) }
            }
        }
    }

}
