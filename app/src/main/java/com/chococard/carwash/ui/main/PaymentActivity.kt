package com.chococard.carwash.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.Job
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.ui.change.ChangeProfileActivity
import com.chococard.carwash.util.Commons
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.*
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : BaseActivity<MainViewModel, MainFactory>(), OnAttachListener {

    override fun viewModel() = MainViewModel::class.java

    override fun factory() = MainFactory(MainRepository(MainApi.invoke(baseContext)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val job = intent.getParcelableExtra(Commons.JOB) as Job

        setToolbar(toolbar)

        // set widget
        val (_, fullName, image, _, _, service, price, beginLat, beginLong, endLat, endLong, _, _, dateTime) = job
        tv_date_time.text = dateTime
        tv_full_name.text = fullName
        tv_service.text = service
        tv_price.text = price
        image?.let { iv_photo.loadCircle(it) }

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        fab.setOnClickListener { navigation(beginLat, beginLong, endLat, endLong) }

        bt_payment.setOnClickListener {
            PaymentDialog().show(supportFragmentManager, null)
        }

        //observe
        viewModel.payment.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                writePref(Commons.JOB_FLAG, Commons.JOB_FLAG_OFF)
                finish()
            } else {
                message?.let { toast(it) }
            }
        })

        viewModel.exception.observe(this, Observer {
            progress_bar.hide()
            toast(it, Toast.LENGTH_LONG)
        })
    }

    private fun navigation(
        beginLat: Double?,
        beginLong: Double?,
        endLat: Double?,
        endLong: Double?
    ) = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(
            getString(
                R.string.google_maps_navigation,
                beginLat,
                beginLong,
                endLat,
                endLong
            )
        )
        startActivity(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                Intent(baseContext, ChangeProfileActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.option_change_password -> toast(getString(R.string.not_available))
            R.id.option_contact_admin -> contactAdmin()
            R.id.option_logout -> toast(getString(R.string.not_available))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = toast(getString(R.string.not_available), Toast.LENGTH_LONG)

    override fun onAttach(data: String) {
        progress_bar.show()
        viewModel.payment(data)
    }

}
