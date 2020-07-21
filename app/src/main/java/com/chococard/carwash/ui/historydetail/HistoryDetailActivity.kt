package com.chococard.carwash.ui.historydetail

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.ui.viewimage.ViewImageActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.HistoryDetailViewModel
import kotlinx.android.synthetic.main.activity_history_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryDetailActivity : BaseActivity() {

    val viewModel: HistoryDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        init()
    }

    private fun init() {
        //set toolbar
        setToolbar(toolbar)

        // get data history fragment
        val history = intent.getParcelableExtra<History>(CommonsConstant.HISTORY)
        if (history == null) finish()
        val (_, fullName, imageProfile, packageName, location, vehicleRegistration, price,
            jobDateTime, imagesBeforeService, imagesAfterService, otherImagesService, comment) = history!!

        // set widget
        tv_full_name.text = fullName
        tv_service.text = packageName
        tv_location.text = location
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

        // observe
        viewModel.getLogout.observe(this, Observer { response ->
            val (success, message) = response
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        })

        viewModel.getError.observe(this, Observer {
            dialogError(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> startActivity<ChangeProfileActivity>()
            R.id.option_change_password -> startActivity<ChangePasswordActivity>()
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout { viewModel.callLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

}
