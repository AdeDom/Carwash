package com.chococard.carwash.ui.serviceinfo

import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceInfoViewModel
import kotlinx.android.synthetic.main.activity_service_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceInfoActivity : BaseLocationActivity() {

    val viewModel: ServiceInfoViewModel by viewModel()
    private var mLatLngCustomer: Pair<Double, Double>? = null

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
            tv_phone.text = phone.toUnderline()
            if (latitude != null && longitude != null) {
                tv_location.text = getLocality(latitude, longitude).toUnderline()
                mLatLngCustomer = Pair(latitude, longitude)
            }
            tv_vehicle_registration.text = vehicleRegistration
            tv_price.text = price
            iv_photo.setImageCircle(imageProfile)

            // set event
            tv_phone.setOnClickListener {
                startActivityActionDial(phone)
            }
        })

        viewModel.getServiceNavigation.observe(this, Observer { navigation ->
            val (latitude, longitude) = navigation
            tv_location.setOnClickListener {
                startActivityGoogleMapNavigation(
                    latitude,
                    longitude,
                    mLatLngCustomer?.first,
                    mLatLngCustomer?.second
                )
            }
        })

        viewModel.getError.observe(this, Observer {
            dialogError(it)
        })
    }

    override fun onLocationChanged(location: Location?) {
        if (location == null) return

        val navigation = Pair(location.latitude, location.longitude)
        viewModel.setValueServiceNavigation(navigation)
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
