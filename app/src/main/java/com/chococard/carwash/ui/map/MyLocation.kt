package com.chococard.carwash.ui.map

import android.content.Context
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.setMyLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MyLocation(
    context: Context,
    googleMap: GoogleMap?,
    latLng: LatLng,
    user: User?
) {

    init {
        MapFragment.sMarkerMyLocation?.remove()

        context.setImageCircle(user?.image) {
            if (googleMap != null) {
                MapFragment.sMarkerMyLocation = googleMap.addMarker(
                    MarkerOptions().apply {
                        position(latLng)
                        icon(BitmapDescriptorFactory.fromBitmap(context.setMyLocation(it)))
                        title(user?.fullName)
                    }
                )
            }
        }
    }

}
