package com.chococard.carwash.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.util.JobFlag
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.activity_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : BaseActivity(), FlagPaymentListener {

    val viewModel: PaymentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setToolbar(toolbar)

        viewModel.getDbJob.observe(this, Observer { job ->
            if (job == null) return@Observer
            val (_, fullName, image, _, _, service, price, beginLat, beginLong, endLat, endLong, _, _, dateTime) = job
            tv_date_time.text = dateTime
            tv_full_name.text = fullName
            tv_service.text = service
            tv_price.text = price
            image?.let { iv_photo.setImageCircle(it) }

            fab.setOnClickListener { navigation(beginLat, beginLong, endLat, endLong) }
        })

        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_payment.setOnClickListener {
            PaymentDialog().show(supportFragmentManager, null)
        }

        //observe
        viewModel.getPayment.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                writeJobFlag(JobFlag.JOB_FLAG_OFF)
                viewModel.deleteJob()
                finish()
            } else {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun navigation(
        beginLat: Double?,
        beginLong: Double?,
        endLat: Double?,
        endLong: Double?
    ) = startActivity(
        Intent.ACTION_VIEW, getString(
            R.string.google_maps_navigation,
            beginLat,
            beginLong,
            endLat,
            endLong
        )
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                startActivity<ChangeProfileActivity>()
            }
            R.id.option_change_password -> toast(getString(R.string.not_available))
            R.id.option_contact_admin -> dialogContactAdmin()
            R.id.option_logout -> toast(getString(R.string.not_available))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = toast(getString(R.string.not_available), Toast.LENGTH_LONG)

    override fun onFlag(flag: Int) {
        progress_bar.show()
        viewModel.callPayment(flag)
    }

}
