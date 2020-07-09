package com.chococard.carwash.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import com.chococard.carwash.util.CommonsConstant
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

abstract class BaseLocationActivity : BaseActivity(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setReceiverLocation()
        setRequestLocation()
    }

    override fun onResume() {
        super.onResume()
        //Register receiver.
        broadcastReceiver(true)

        mGoogleApiClient.connect()
        if (mGoogleApiClient.isConnected) startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        //Unregister receiver.
        broadcastReceiver(false)

        if (mGoogleApiClient.isConnected) stopLocationUpdate()
        if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
    }

    // When location is not enabled, the application will end.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isLocationProviderEnabled = Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
        if (!isLocationProviderEnabled && requestCode == CommonsConstant.REQUEST_CODE_LOCATION)
            finishAffinity()
    }

    private fun setReceiverLocation() {
        mBroadcastReceiver = object : BroadcastReceiver() {
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

        settingLocation()
    }

    // Set up receiver register & unregister.
    private fun broadcastReceiver(isReceiver: Boolean) {
        if (isReceiver) {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mBroadcastReceiver, filter)
        } else {
            unregisterReceiver(mBroadcastReceiver)
        }
    }

    // If location off give on setting on.
    private fun settingLocation() {
        val isLocationProviderEnabled = Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
        if (!isLocationProviderEnabled) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                startActivityForResult(this, CommonsConstant.REQUEST_CODE_LOCATION)
            }
        }
    }

    private fun setRequestLocation() {
        mGoogleApiClient = GoogleApiClient.Builder(baseContext)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient.connect()

        mLocationRequest = LocationRequest()
            .setInterval(10_000)
            .setFastestInterval(8_000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    private fun startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }

    private fun stopLocationUpdate() =
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)

    override fun onConnected(p0: Bundle?) = startLocationUpdate()

    override fun onConnectionSuspended(p0: Int) = mGoogleApiClient.connect()

    override fun onConnectionFailed(p0: ConnectionResult) {}

}
