package com.chococard.carwash.ui.navigation

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.networks.request.SetNavigationRequest
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.ui.service.ServiceActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.NavigationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationActivity : BaseLocationActivity(), OnMapReadyCallback {

    val viewModel: NavigationViewModel by viewModel()
    private var mGoogleMap: GoogleMap? = null
    private var mIsFlagMoveCamera: Boolean = true
    private var mMarkerMyLocation: Marker? = null
    private var mMarkerCustomer: Marker? = null
    private var mJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setToolbar(toolbar)

        val mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this@NavigationActivity)

        bt_service.setOnClickListener { serviceJob() }

        // observe
        viewModel.getDbJob.observe(this, Observer { job ->
            if (job == null) return@Observer
            mJob = job
        })

        viewModel.getNavigationResponse.observe(this, Observer { response ->
            val (success, message, navigation) = response
            if (success) {
                if (navigation?.customerLatitude != null && navigation.customerLongitude != null) {
                    val latLng = LatLng(navigation.customerLatitude, navigation.customerLongitude)
                    setLocationCustomer(latLng)
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getJobStatusName.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                startActivity<ServiceActivity>()
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            dialogError(it)
        })
    }

    private fun serviceJob() {
        progress_bar.show()
        viewModel.callSetJobStatusName()
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

        if (mJob?.latitude != null && mJob?.longitude != null) {
            val latLngCustomer = LatLng(mJob?.latitude!!, mJob?.longitude!!)
            fab.setOnClickListener { navigation(latLng, latLngCustomer) }
        }

        viewModel.getDbUser.observe(this, Observer { user ->
            if (user == null) return@Observer

            mMarkerMyLocation?.remove()

            baseContext.setImageCircle(user.image) { bitmap ->
                val bmp = baseContext.setImageMarkerCircle(bitmap)
                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
                mMarkerMyLocation = mGoogleMap?.addMarker(
                    MarkerOptions().apply {
                        position(latLng)
                        icon(bitmapDescriptor)
                        title(user.fullName)
                    }
                )
            }
        })

        val setNavigation = SetNavigationRequest(latLng.latitude, latLng.longitude)
        viewModel.callSetNavigation(setNavigation)
    }

    private fun setLocationCustomer(latLng: LatLng) {
        mMarkerCustomer?.remove()

        baseContext.setImageCircle(mJob?.imageProfile) { bitmap ->
            val bmp = baseContext.setImageMarkerCircle(bitmap)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
            mMarkerCustomer = mGoogleMap?.addMarker(
                MarkerOptions().apply {
                    position(latLng)
                    icon(bitmapDescriptor)
                    title(mJob?.fullName)
                }
            )
        }
    }

    private fun navigation(beginLatLng: LatLng?, endLatLng: LatLng?) {
        startActivity(
            Intent.ACTION_VIEW, getString(
                R.string.google_maps_navigation,
                beginLatLng?.latitude,
                beginLatLng?.longitude,
                endLatLng?.latitude,
                endLatLng?.longitude
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> {
                startActivity(Intent.ACTION_DIAL, getString(R.string.contact_admin_tel))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
