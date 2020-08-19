package com.chococard.carwash.ui.navigation

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.UserInfo
import com.chococard.carwash.data.networks.request.SetNavigationRequest
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.ui.service.ServiceActivity
import com.chococard.carwash.ui.serviceinfo.ServiceInfoActivity
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.NavigationViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationActivity : BaseLocationActivity(), OnMapReadyCallback {

    val viewModel by viewModel<NavigationViewModel>()
    private var mGoogleMap: GoogleMap? = null
    private var mIsFlagMoveCamera: Boolean = true
    private var mMarkerMyLocation: Marker? = null
    private var mMarkerCustomer: Marker? = null
    private var mDbUser: UserInfo? = null
    private var mDbJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setToolbar(toolbar)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this@NavigationActivity)

        // set event
        fab_main.setOnClickListener { setFabMenuVisibility() }

        view_shadow.setOnClickListener { setFabMenuVisibility() }

        fab_arrive.setOnClickListener {
            setFabMenuVisibility()
            viewModel.callJobStatusService()
        }

        fab_service_info.setOnClickListener {
            setFabMenuVisibility()
            startActivity<ServiceInfoActivity>()
        }

        fab_call.setOnClickListener {
            setFabMenuVisibility()
            startActivityActionDial(mDbJob?.phone)
        }

        // observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.getDbJobLiveData.observe { job ->
            mDbJob = job
        }

        viewModel.getDbUserLiveData.observe { userInfo ->
            if (userInfo == null) return@observe
            mDbUser = userInfo
        }

        viewModel.getNavigation.observe { response ->
            val (success, message, navigation) = response
            if (success) {
                if (navigation?.customerLatitude != null && navigation.customerLongitude != null) {
                    val latLng = LatLng(navigation.customerLatitude, navigation.customerLongitude)
                    setLocationCustomer(latLng)
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.getJobStatusService.observe { response ->
            val (success, message) = response
            if (success) startActivity<ServiceActivity>() else root_layout.snackbar(message)
        }

        viewModel.error.observeError()
    }

    private fun setFabMenuVisibility() {
        if (fab_main.tag == FlagConstant.FAB_VISIBILITY_OFF) {
            fab_main.tag = FlagConstant.FAB_VISIBILITY_ON
            fab_main.setImageResource(R.drawable.ic_menu_white)
            view_shadow.hide()
            startAnimationFabClose(layout_arrive)
            startAnimationFabClose(layout_navigation)
            startAnimationFabClose(layout_service_info)
            startAnimationFabClose(layout_call)
        } else {
            fab_main.tag = FlagConstant.FAB_VISIBILITY_OFF
            fab_main.setImageResource(R.drawable.ic_close_white)
            view_shadow.show()
            startAnimationFabOpen(layout_arrive)
            startAnimationFabOpen(layout_navigation)
            startAnimationFabOpen(layout_service_info)
            startAnimationFabOpen(layout_call)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap
        mGoogleMap?.isMyLocationEnabled = true
        mGoogleMap?.setMinZoomPreference(12F)
        mGoogleMap?.setMaxZoomPreference(16F)

        mGoogleMap.animateCamera()
    }

    override fun onLocationResult(location: Location) {
        super.onLocationResult(location)
        val latLng = LatLng(location.latitude, location.longitude)

        if (mIsFlagMoveCamera) {
            mIsFlagMoveCamera = false
            mGoogleMap.animateCamera(latLng)
        }

        fab_navigation.setOnClickListener {
            setFabMenuVisibility()
            startActivityGoogleMapNavigation(
                beginLatitude = location.latitude,
                beginLongitude = location.longitude,
                endLatitude = mDbJob?.latitude,
                endLongitude = mDbJob?.longitude
            )
        }

        mMarkerMyLocation?.remove()
        setImageCircle(mDbUser?.image) { bitmap ->
            val bmp = setImageMarkerCircle(bitmap)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
            mMarkerMyLocation = mGoogleMap?.addMarker(
                MarkerOptions().apply {
                    position(latLng)
                    icon(bitmapDescriptor)
                    title(mDbUser?.fullName)
                }
            )
        }

        val setNavigation = SetNavigationRequest(latLng.latitude, latLng.longitude)
        viewModel.callSetNavigation(setNavigation)
    }

    private fun setLocationCustomer(latLng: LatLng) {
        mMarkerCustomer?.remove()

        setImageCircle(mDbJob?.imageProfile) { bitmap ->
            val bmp = setImageMarkerCircle(bitmap)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp)
            mMarkerCustomer = mGoogleMap?.addMarker(
                MarkerOptions().apply {
                    position(latLng)
                    icon(bitmapDescriptor)
                    title(mDbJob?.fullName)
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_report -> startActivity<ReportActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

}
