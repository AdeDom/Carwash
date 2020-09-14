package com.chococard.carwash.ui.serviceinfo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.awaitLastLocation
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceInfoViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_service_info.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceInfoActivity : BaseActivity() {

    val viewModel by viewModel<ServiceInfoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_info)

        setToolbar(toolbar)

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        // observe
        viewModel.getDbJobLiveData.observe(this, { job ->
            if (job == null) return@observe
            // set widget
            val (_, _, fullName, imageProfile, phone, packageName, price, vehicleRegistration, _, _, location, _, dateTime) = job
            tv_date_time.text = dateTime
            tv_full_name.text = fullName
            tv_service.text = packageName
            tv_phone.text = phone.toUnderline()
            tv_location.text = location.toUnderline()
            tv_vehicle_registration.text = vehicleRegistration
            tv_price.text = price
            iv_photo.setImageCircle(imageProfile)

            // set event
            tv_phone.setOnClickListener {
                startActivityActionDial(phone)
            }
        })

        viewModel.serviceNavigation.observe { serviceNavigation ->
            startActivityGoogleMapNavigation(
                beginLatitude = serviceNavigation.beginLatitude,
                beginLongitude = serviceNavigation.beginLongitude,
                endLatitude = serviceNavigation.endLatitude,
                endLongitude = serviceNavigation.endLongitude
            )
        }

        tv_location.setOnClickListener {
            Coroutines.main {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                val beginLatitude = fusedLocationClient.awaitLastLocation().latitude
                val beginLongitude = fusedLocationClient.awaitLastLocation().longitude
                viewModel.setServiceNavigation(beginLatitude, beginLongitude)
            }
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
