package com.chococard.carwash.ui.historydetail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.gone
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.startActivity
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
        if (history == null) finish()
        val (_, fullName, imageProfile, packageName, latitude, longitude, vehicleRegistration, price,
            jobDateTime, imagesBeforeService, imagesAfterService, otherImagesService, comment) = history!!

        // set widget
        tv_full_name.text = fullName
        tv_service.text = packageName
        if (latitude != null && longitude != null)
            tv_location.text = getLocality(latitude, longitude)
        tv_vehicle_registration.text = vehicleRegistration
        tv_price.text = price
        tv_date_time.text = jobDateTime
        iv_photo.setImageCircle(imageProfile)
        tv_comment.text = comment

        // recycler view
        // before
        if (imagesBeforeService?.size == 0) {
            card_before_image.gone()
        } else {
            val adtImagesBeforeService = HistoryDetailAdapter()
            recycler_view_before_service.apply {
                layoutManager = LinearLayoutManager(baseContext)
                adapter = adtImagesBeforeService
            }
            adtImagesBeforeService.setList(imagesBeforeService)
            adtImagesBeforeService.onClick = { beforeService ->
                startActivity<ViewImageActivity> {
                    it.putExtra(CommonsConstant.IMAGE, beforeService.image)
                }
            }
        }

        // after
        if (imagesAfterService?.size == 0) {
            card_after_image.gone()
        } else {
            val adtImagesAfterService = HistoryDetailAdapter()
            recycler_view_after_service.apply {
                layoutManager = LinearLayoutManager(baseContext)
                adapter = adtImagesAfterService
            }
            adtImagesAfterService.setList(imagesAfterService)
            adtImagesAfterService.onClick = { afterService ->
                startActivity<ViewImageActivity> {
                    it.putExtra(CommonsConstant.IMAGE, afterService.image)
                }
            }
        }

        // other image
        if (otherImagesService?.size == 0) {
            card_other_image.gone()
        } else {
            val adtOtherImageService = HistoryDetailAdapter()
            recycler_view_other_image.apply {
                layoutManager = LinearLayoutManager(baseContext)
                adapter = adtOtherImageService
            }
            adtOtherImageService.setList(otherImagesService)
            adtOtherImageService.onClick = { otherImage ->
                startActivity<ViewImageActivity> {
                    it.putExtra(CommonsConstant.IMAGE, otherImage.image)
                }
            }
        }

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
    }

}
