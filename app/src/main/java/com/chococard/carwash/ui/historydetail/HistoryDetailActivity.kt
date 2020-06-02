package com.chococard.carwash.ui.historydetail

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.setImageFromInternet
import kotlinx.android.synthetic.main.activity_history_detail.*

class HistoryDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        init()
    }

    private fun init() {
        // get data history fragment
        val history = intent.getParcelableExtra<History>(CommonsConstant.HISTORY)
        if (history != null) {
            val (_, fullName, imageProfile, packageName, latitude, longitude, vehicleRegistration, price,
                jobDateTime, imageFront, imageBack, imageLeft, imageRight, _, comment) = history

            // set widget
            tv_full_name.text = fullName
            tv_service.text = packageName
            if (latitude != null && longitude != null)
                tv_location.text = getLocality(latitude, longitude)
            tv_vehicle_registration.text = vehicleRegistration
            tv_price.text = price
            tv_date_time.text = jobDateTime
            imageProfile?.let { iv_photo.setImageCircle(it) }
            iv_image_front.setImageFromInternet(imageFront)
            iv_image_back.setImageFromInternet(imageBack)
            iv_image_left.setImageFromInternet(imageLeft)
            iv_image_right.setImageFromInternet(imageRight)
            tv_comment.text = comment
        } else {
            finish()
        }

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

    }

}
