package com.chococard.carwash.ui.map

import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.MapFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.readPref
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.MapViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson

class MapFragment : BaseFragment<MapViewModel, MapFactory>(
    R.layout.fragment_map
), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocationRequest: LocationRequest
    private var mGoogleMap: GoogleMap? = null
    private var mMapView: MapView? = null
    private var mIsFlagMoveCamera: Boolean = true

    override fun viewModel() = MapViewModel::class.java

    override fun factory() = MapFactory(repositoryHeader)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mMapView = view.findViewById(R.id.map_view)
        mMapView?.onCreate(savedInstanceState)
        mMapView?.getMapAsync(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        setRequestLocation()

        // observe
        viewModel.getEmployeeLocation.observe(viewLifecycleOwner, Observer { response ->
            val (success, message, employeeLocation) = response
            if (success) {
                employeeLocation?.let { Employee(requireContext(), mGoogleMap, it) }
            } else {
                message?.let { context.toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getError.observe(viewLifecycleOwner, Observer {
            dialogError(it)
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.setMinZoomPreference(12F)
        mGoogleMap?.setMaxZoomPreference(16F)
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null) return
        val latLng = LatLng(location.latitude, location.longitude)

        if (mIsFlagMoveCamera) {
            mIsFlagMoveCamera = false
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14F)
            mGoogleMap?.moveCamera(cameraUpdate)
        }

        val user = Gson().fromJson(context?.readPref(CommonsConstant.USER), User::class.java)
        MyLocation(requireContext(), mGoogleMap, latLng, user)

        // call api
        viewModel.callSetLocation(latLng.latitude, latLng.longitude)
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
        mGoogleApiClient.connect()
        if (mGoogleApiClient.isConnected) startLocationUpdate()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
        if (mGoogleApiClient.isConnected) stopLocationUpdate()
        if (mGoogleApiClient.isConnected) mGoogleApiClient.disconnect()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
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

    companion object {
        var sMarkerMyLocation: Marker? = null
        val sMarkerEmployee = ArrayList<Marker>()
    }

}
