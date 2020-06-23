package com.chococard.carwash.ui.navigation

import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.request.SetNavigationRequest
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.ui.service.ServiceActivity
import com.chococard.carwash.ui.serviceinfo.ServiceInfoActivity
import com.chococard.carwash.util.FlagConstant
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
    private var mDbUser: User? = null
    private var mDbJob: Job? = null
    private var mFabCloseAnim: Animation? = null
    private var mFabOpenAnim: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setToolbar(toolbar)

        val mapFragment = fragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        mapFragment.getMapAsync(this@NavigationActivity)

        // set fab
        mFabOpenAnim = AnimationUtils.loadAnimation(baseContext, R.anim.fab_open)
        mFabCloseAnim = AnimationUtils.loadAnimation(baseContext, R.anim.fab_close)
        fab_main.tag = FlagConstant.FAB_VISIBILITY_ON

        // set event
        fab_main.setOnClickListener { setFabMenuVisibility() }

        view_shadow.setOnClickListener { setFabMenuVisibility() }

        fab_arrive.setOnClickListener {
            setFabMenuVisibility()
            arriveService()
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
        viewModel.getDbJob.observe(this, Observer { job ->
            if (job == null) return@Observer
            mDbJob = job
        })

        viewModel.getDbUser.observe(this, Observer { user ->
            if (user == null) return@Observer
            mDbUser = user
        })

        viewModel.getNavigation.observe(this, Observer { response ->
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

        viewModel.getJobStatusService.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                startActivity<ServiceActivity>()
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun setFabMenuVisibility() {
        if (fab_main.tag == FlagConstant.FAB_VISIBILITY_ON) {
            fab_main.tag = FlagConstant.FAB_VISIBILITY_OFF
            fab_main.setImageResource(R.drawable.ic_close_white)
            view_shadow.show()
            layout_arrive.startAnimation(mFabOpenAnim)
            layout_navigation.startAnimation(mFabOpenAnim)
            layout_service_info.startAnimation(mFabOpenAnim)
            layout_call.startAnimation(mFabOpenAnim)
        } else {
            fab_main.tag = FlagConstant.FAB_VISIBILITY_ON
            fab_main.setImageResource(R.drawable.ic_menu_white)
            view_shadow.hide()
            layout_arrive.startAnimation(mFabCloseAnim)
            layout_navigation.startAnimation(mFabCloseAnim)
            layout_service_info.startAnimation(mFabCloseAnim)
            layout_call.startAnimation(mFabCloseAnim)
        }
    }

    private fun arriveService() {
        progress_bar.show()
        viewModel.callJobStatusService()
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

        val latitude = mDbJob?.latitude
        val longitude = mDbJob?.longitude
        if (latitude != null && longitude != null) {
            fab_navigation.setOnClickListener {
                setFabMenuVisibility()
                startActivityGoogleMapNavigation(
                    location.latitude,
                    location.longitude,
                    latitude,
                    longitude
                )
            }
        }

        mMarkerMyLocation?.remove()
        baseContext.setImageCircle(mDbUser?.image) { bitmap ->
            val bmp = baseContext.setImageMarkerCircle(bitmap)
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

        baseContext.setImageCircle(mDbJob?.imageProfile) { bitmap ->
            val bmp = baseContext.setImageMarkerCircle(bitmap)
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
            R.id.option_contact_admin -> startActivityActionDial()
        }
        return super.onOptionsItemSelected(item)
    }

}
