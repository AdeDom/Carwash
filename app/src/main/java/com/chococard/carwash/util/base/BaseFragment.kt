package com.chococard.carwash.util.base

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

abstract class BaseFragment<VM : ViewModel, F : ViewModelProvider.NewInstanceFactory>(
    private val layout: Int
) : Fragment(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    protected lateinit var viewModel: VM
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, factory()).get(viewModel())

        setRequestLocation()
    }

    abstract fun viewModel(): Class<VM>

    abstract fun factory(): F

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
        if (mGoogleApiClient.isConnected) startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        if (mGoogleApiClient.isConnected) stopLocationUpdate()
        if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
    }

    private fun setRequestLocation() {
        mGoogleApiClient = GoogleApiClient.Builder(requireContext())
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

    override fun onLocationChanged(location: Location?) {}

}
