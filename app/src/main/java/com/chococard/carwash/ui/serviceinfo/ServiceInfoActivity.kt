package com.chococard.carwash.ui.serviceinfo

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.startActivityActionDial
import com.chococard.carwash.viewmodel.ServiceInfoViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_service_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceInfoActivity : BaseLocationActivity() {

    val viewModel: ServiceInfoViewModel by viewModel()
    private var mLatLngCustomer: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_info)

        setToolbar(toolbar)

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        // observe
        viewModel.getDbJob.observe(this, Observer { job ->
            if (job == null) return@Observer

            // set widget
            val (_, fullName, imageProfile, phone, packageName, price, vehicleRegistration, latitude, longitude, _, dateTime) = job
            tv_date_time.text = dateTime
            tv_full_name.text = fullName
            tv_service.text = packageName
            tv_phone.text = phone
            if (latitude != null && longitude != null) {
                tv_location.text = getLocality(latitude, longitude)
                mLatLngCustomer = LatLng(latitude, longitude)
            }
            tv_vehicle_registration.text = vehicleRegistration
            tv_price.text = price
            iv_photo.setImageCircle(imageProfile)

            // set event
            tv_phone.setOnClickListener {
                startActivityActionDial(phone)
            }
        })

        viewModel.getError.observe(this, Observer {
            dialogError(it)
        })
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null) return
        val latLng = LatLng(location.latitude, location.longitude)

        tv_location.setOnClickListener {
            navigation(latLng, mLatLngCustomer)
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
            R.id.option_contact_admin -> startActivityActionDial()
        }
        return super.onOptionsItemSelected(item)
    }

}
