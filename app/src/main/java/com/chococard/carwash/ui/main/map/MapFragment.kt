package com.chococard.carwash.ui.main.map

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class MapFragment : BaseFragment<MapViewModel, MapFactory>(
    R.layout.fragment_map
), OnMapReadyCallback {

    private var mGoogleMap: GoogleMap? = null
    private var mMapView: MapView? = null

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

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.isMyLocationEnabled = true

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(13.6970213, 100.6083957), 15.0F)
        mGoogleMap?.animateCamera(cameraUpdate)
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

}
