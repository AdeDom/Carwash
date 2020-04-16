package com.chococard.carwash.ui.main.map

import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseFragment
import com.chococard.carwash.util.extension.readPref
import com.chococard.carwash.util.extension.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson

class MapFragment : BaseFragment<MapViewModel, MapFactory>(
    R.layout.fragment_map
), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var mMapView: MapView? = null
    private var mIsCamera: Boolean = true

    override fun viewModel() = MapViewModel::class.java

    override fun factory() = MapFactory(MainRepository(MainApi.invoke(requireContext())))

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
        // observe
        viewModel.employeeLocation.observe(viewLifecycleOwner, Observer { response ->
            val (success, message, employeeLocation) = response
            if (success) {
                employeeLocation?.let { Employee(requireContext(), mGoogleMap, it) }
            } else {
                message?.let { context?.toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer {
            context?.toast(it, Toast.LENGTH_LONG)
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.setMinZoomPreference(12F)
        mGoogleMap?.setMaxZoomPreference(16F)
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        if (location == null) return
        val latLng = LatLng(location.latitude, location.longitude)

        if (mIsCamera) {
            mIsCamera = false
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14F)
            mGoogleMap?.moveCamera(cameraUpdate)
        }

        val user = Gson().fromJson(context?.readPref(R.string.user), User::class.java)
        MyLocation(requireContext(), mGoogleMap, latLng, user)

        // call api
        viewModel.setLocation(latLng.latitude, latLng.longitude)

    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    companion object {
        var sMarkerMyLocation: Marker? = null
        val sMarkerEmployee = ArrayList<Marker>()
    }

}
