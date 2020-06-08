package com.chococard.carwash.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.activity_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : BaseActivity() {

    val viewModel: PaymentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setToolbar(toolbar)

        // set widgets
        viewModel.getDbJob.observe(this, Observer { job ->
            if (job == null) return@Observer
            val (_, fullName, imageProfile, packageName, price, _, _, _, dateTime) = job
            tv_date_time.text = dateTime
            tv_full_name.text = fullName
            tv_service.text = packageName
            tv_price.text = price
            iv_photo.setImageCircle(imageProfile)
        })

        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_payment.setOnClickListener { viewModel.callPayment() }

        //observe
        viewModel.getPayment.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                viewModel.deleteDbJob()
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
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
