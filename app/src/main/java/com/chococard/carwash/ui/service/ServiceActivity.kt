package com.chococard.carwash.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.ServiceImage
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.activity_service.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceActivity : BaseActivity() {

    val viewModel: ServiceViewModel by viewModel()
    private var mImageUrlFront: String? = null
    private var mImageUrlBack: String? = null
    private var mImageUrlLeft: String? = null
    private var mImageUrlRight: String? = null

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

        bt_payment.setOnClickListener {
            if (mImageUrlFront != null && mImageUrlBack != null && mImageUrlLeft != null && mImageUrlRight != null) {
                startActivity<PaymentActivity>()
            } else {
                dialogError(getString(R.string.error_empty_image))
            }
        }

        // call api
        progress_bar.show()
        viewModel.callFetchImageService()

        // observe
        viewModel.getServiceImage.observe(this, Observer { response ->
            val (success, message, serviceImage) = response
            if (success) {
                serviceImage?.let { setImageJobService(it) }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getImageService.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message, serviceImage) = response
            if (success) {
                serviceImage?.let { setImageJobService(it) }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            progress_bar_front.hide()
            progress_bar_back.hide()
            progress_bar_left.hide()
            progress_bar_right.hide()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val multipartBody = convertToMultipartBody(data.data!!)
            var statusService: RequestBody? = null
            when (requestCode) {
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT -> {
                    iv_camera_front.visibility = View.INVISIBLE
                    progress_bar_front.show()
                    statusService = RequestBody.create(
                        MultipartBody.FORM,
                        FlagConstant.STATUS_SERVICE_FRONT.toString()
                    )
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK -> {
                    iv_camera_back.visibility = View.INVISIBLE
                    progress_bar_back.show()
                    statusService = RequestBody.create(
                        MultipartBody.FORM,
                        FlagConstant.STATUS_SERVICE_BACK.toString()
                    )
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT -> {
                    iv_camera_left.visibility = View.INVISIBLE
                    progress_bar_left.show()
                    statusService = RequestBody.create(
                        MultipartBody.FORM,
                        FlagConstant.STATUS_SERVICE_LEFT.toString()
                    )
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT -> {
                    iv_camera_right.visibility = View.INVISIBLE
                    progress_bar_right.show()
                    statusService = RequestBody.create(
                        MultipartBody.FORM,
                        FlagConstant.STATUS_SERVICE_RIGHT.toString()
                    )
                }
            }

            if (statusService != null)
                viewModel.callUploadImageService(multipartBody, statusService)
        }
    }

    private fun setImageJobService(serviceImage: ServiceImage) {
        val (front, back, left, right, otherImage) = serviceImage
        mImageUrlFront = front
        mImageUrlBack = back
        mImageUrlLeft = left
        mImageUrlRight = right

        setImageView(front, iv_image_front, iv_camera_front, progress_bar_front)
        setImageView(back, iv_image_back, iv_camera_back, progress_bar_back)
        setImageView(left, iv_image_left, iv_camera_left, progress_bar_left)
        setImageView(right, iv_image_right, iv_camera_right, progress_bar_right)
    }

    private fun setImageView(
        url: String?,
        iv_image: ImageView,
        iv_camera: ImageView,
        progress_bar: ProgressBar
    ) {
        if (url != null) {
            progress_bar.hide()
            iv_camera.visibility = View.INVISIBLE
            iv_image.setImageFromInternet(url)
        } else {
            iv_camera.visibility = View.VISIBLE
        }
    }

}
