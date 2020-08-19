package com.chococard.carwash.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.asLiveData
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.util.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate

abstract class BaseLocationActivity : BaseActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled =
                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //NETWORK_PROVIDER

                if (!isGpsEnabled)
                    settingLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startUpdatingLocation()
        settingLocation()
    }

    override fun onResume() {
        super.onResume()
        //Register receiver.
        broadcastReceiver(true)
    }

    override fun onPause() {
        super.onPause()
        //Unregister receiver.
        broadcastReceiver(false)
    }

    private fun startUpdatingLocation() {
        fusedLocationClient.locationFlow()
            .conflate()
            .catch { e ->
                toast(e.message)
            }
            .asLiveData()
            .observe {
                onLocationResult(it)
            }
    }

    protected open fun onLocationResult(location: Location) {}

    // When location is not enabled, the application will end.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (!isLocationProviderEnabled() && requestCode == CommonsConstant.REQUEST_CODE_LOCATION)
            finishAffinity()
    }

    // Set up receiver register & unregister.
    private fun broadcastReceiver(isReceiver: Boolean) {
        if (isReceiver) {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(broadcastReceiver, filter)
        } else {
            unregisterReceiver(broadcastReceiver)
        }
    }

    // If location off give on setting on.
    private fun settingLocation() {
        if (!isLocationProviderEnabled()) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                startActivityForResult(this, CommonsConstant.REQUEST_CODE_LOCATION)
            }
        }
    }

    private fun isLocationProviderEnabled(): Boolean {
        return Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
    }

}
