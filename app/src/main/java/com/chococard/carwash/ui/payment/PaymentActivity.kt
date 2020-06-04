package com.chococard.carwash.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.activity_payment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentActivity : BaseActivity(), FlagPaymentListener {

    val viewModel: PaymentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        setToolbar(toolbar)

        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_payment.setOnClickListener {
            PaymentDialog().show(supportFragmentManager, null)
        }

        //observe
        viewModel.getPayment.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
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
            R.id.option_contact_admin -> {
                startActivity(Intent.ACTION_DIAL, getString(R.string.contact_admin_tel))
            }
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
