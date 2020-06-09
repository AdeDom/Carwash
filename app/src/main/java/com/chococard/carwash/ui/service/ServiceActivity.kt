package com.chococard.carwash.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.selectImage
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        init()
    }

    private fun init() {
        // set toolbar
        setToolbar(toolbar)

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_image_front.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_FRONT) }

        iv_image_back.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_BACK) }

        iv_image_left.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_LEFT) }

        iv_image_right.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_RIGHT) }

        bt_report.setOnClickListener { startActivity<ReportActivity>() }

        bt_payment.setOnClickListener { startActivity<PaymentActivity>() }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT -> {
                    toast(data.data.toString())
                    iv_camera_front.visibility = View.INVISIBLE
                    progress_bar_front.show()
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK -> {
                    toast(data.data.toString())
                    iv_camera_back.visibility = View.INVISIBLE
                    progress_bar_back.show()
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT -> {
                    toast(data.data.toString())
                    iv_camera_left.visibility = View.INVISIBLE
                    progress_bar_left.show()
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT -> {
                    toast(data.data.toString())
                    iv_camera_right.visibility = View.INVISIBLE
                    progress_bar_right.show()
                }
            }
        }
    }

}
