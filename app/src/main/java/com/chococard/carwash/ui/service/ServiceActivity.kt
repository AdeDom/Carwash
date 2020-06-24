package com.chococard.carwash.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.ServiceImage
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.ui.report.ReportActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.activity_service.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceActivity : BaseActivity() {

    val viewModel: ServiceViewModel by viewModel()
    private val mListImageService = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        init()
    }

    private fun init() {
        // set toolbar
        setToolbar(toolbar)

        // set widget
        val adt = ServiceAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = adt
        }
        adt.onLongClick = { otherImage ->
            otherImage.imageId?.let { deleteOtherImageService(it) }
            true
        }
        adt.onRemoveOtherImage = { otherImage ->
            otherImage.imageId?.let { deleteOtherImageService(it) }
        }

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_image_front_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_FRONT_BEFORE) }
        iv_image_back_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_BACK_BEFORE) }
        iv_image_left_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_LEFT_BEFORE) }
        iv_image_right_before.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_BEFORE) }
        iv_image_front_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_FRONT_AFTER) }
        iv_image_back_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_BACK_AFTER) }
        iv_image_left_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_LEFT_AFTER) }
        iv_image_right_after.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_AFTER) }

        iv_image_front_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_BEFORE) }
        iv_image_back_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_BEFORE) }
        iv_image_left_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_BEFORE) }
        iv_image_right_before.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_BEFORE) }
        iv_image_front_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_AFTER) }
        iv_image_back_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_AFTER) }
        iv_image_left_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_AFTER) }
        iv_image_right_after.setOnLongClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_AFTER) }

        card_remove_front_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_BEFORE) }
        card_remove_back_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_BEFORE) }
        card_remove_left_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_BEFORE) }
        card_remove_right_before.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_BEFORE) }
        card_remove_front_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_FRONT_AFTER) }
        card_remove_back_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_BACK_AFTER) }
        card_remove_left_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_LEFT_AFTER) }
        card_remove_right_after.setOnClickListener { deleteImageService(FlagConstant.STATUS_SERVICE_RIGHT_AFTER) }

        iv_add_other_image.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE_OTHER_IMAGE) }

        bt_report.setOnClickListener { startActivity<ReportActivity>() }

        bt_payment.setOnClickListener {
            if (mListImageService.size == 8) {
                startActivity<PaymentActivity>()
            } else {
                dialogError(getString(R.string.error_empty_image))
            }
        }

        // call api
        progress_bar.show()
        viewModel.callFetchImageService()

        // observe
        viewModel.getUploadImageService.observe(this, Observer { response ->
            progress_bar_other_image.hide()
            val (success, message, serviceImage) = response
            if (success) {
                adt.setList(serviceImage?.otherImageService)
                serviceImage?.let { setImageJobService(it) }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getImageService.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message, serviceImage) = response
            if (success) {
                adt.setList(serviceImage?.otherImageService)
                serviceImage?.let { setImageJobService(it) }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getDeleteServiceImage.observe(this, Observer { response ->
            val (success, message, serviceImage) = response
            if (success) {
                serviceImage?.let { setImageJobService(it) }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getDeleteServiceOtherImage.observe(this, Observer { response ->
            progress_bar_other_image.hide()
            val (success, message, serviceImage) = response
            if (success) {
                adt.setList(serviceImage?.otherImageService)
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getValidateMaximumOtherImage.observe(this, Observer {
            if (it >= 5) {
                card_add_other_image.hide()
                tv_maximum_other_image.show()
            } else {
                card_add_other_image.show()
                tv_maximum_other_image.hide()
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            progress_bar_front_before.hide()
            progress_bar_back_before.hide()
            progress_bar_left_before.hide()
            progress_bar_right_before.hide()
            progress_bar_right_after.hide()
            progress_bar_right_after.hide()
            progress_bar_right_after.hide()
            progress_bar_right_after.hide()
            progress_bar_other_image.hide()
            dialogError(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        return true
    }

    private fun deleteImageService(statusService: Int): Boolean {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            when (statusService) {
                FlagConstant.STATUS_SERVICE_FRONT_BEFORE -> progress_bar_front_before.show()
                FlagConstant.STATUS_SERVICE_BACK_BEFORE -> progress_bar_back_before.show()
                FlagConstant.STATUS_SERVICE_LEFT_BEFORE -> progress_bar_left_before.show()
                FlagConstant.STATUS_SERVICE_RIGHT_BEFORE -> progress_bar_right_before.show()
                FlagConstant.STATUS_SERVICE_FRONT_AFTER -> progress_bar_front_after.show()
                FlagConstant.STATUS_SERVICE_BACK_AFTER -> progress_bar_back_after.show()
                FlagConstant.STATUS_SERVICE_LEFT_AFTER -> progress_bar_left_after.show()
                FlagConstant.STATUS_SERVICE_RIGHT_AFTER -> progress_bar_right_after.show()
            }
            val deleteImageService = DeleteImageServiceRequest(statusService)
            viewModel.callDeleteServiceImage(deleteImageService)
        }
        return true
    }

    private fun deleteOtherImageService(imageId: Int) {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            progress_bar_other_image.show()
            val deleteImageService = DeleteImageServiceRequest(imageId)
            viewModel.callDeleteServiceOtherImage(deleteImageService)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> startActivityActionDial()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val statusService: Int? = when (requestCode) {
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_BEFORE -> {
                    iv_camera_front_before.hide()
                    progress_bar_front_before.show()
                    FlagConstant.STATUS_SERVICE_FRONT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_BEFORE -> {
                    iv_camera_back_before.hide()
                    progress_bar_back_before.show()
                    FlagConstant.STATUS_SERVICE_BACK_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_BEFORE -> {
                    iv_camera_left_before.hide()
                    progress_bar_left_before.show()
                    FlagConstant.STATUS_SERVICE_LEFT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_BEFORE -> {
                    iv_camera_right_before.hide()
                    progress_bar_right_before.show()
                    FlagConstant.STATUS_SERVICE_RIGHT_BEFORE
                }
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_AFTER -> {
                    iv_camera_front_after.hide()
                    progress_bar_front_after.show()
                    FlagConstant.STATUS_SERVICE_FRONT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_AFTER -> {
                    iv_camera_back_after.hide()
                    progress_bar_back_after.show()
                    FlagConstant.STATUS_SERVICE_BACK_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_AFTER -> {
                    iv_camera_left_after.hide()
                    progress_bar_left_after.show()
                    FlagConstant.STATUS_SERVICE_LEFT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_AFTER -> {
                    iv_camera_right_after.hide()
                    progress_bar_right_after.show()
                    FlagConstant.STATUS_SERVICE_RIGHT_AFTER
                }
                CommonsConstant.REQUEST_CODE_IMAGE_OTHER_IMAGE -> {
                    val count: Int = viewModel.getValueValidateMaximumOtherImage()
                    viewModel.setValueValidateMaximumOtherImage(count.plus(1))
                    progress_bar_other_image.show()
                    FlagConstant.STATUS_SERVICE_OTHER_IMAGE
                }
                else -> null
            }

            viewModel.callUploadImageService(
                convertToMultipartBody(data.data!!),
                statusService.toRequestBody()
            )
        }
    }

    private fun setImageJobService(serviceImage: ServiceImage) {
        val (frontBefore, backBefore, leftBefore, rightBefore, frontAfter, backAfter, leftAfter, rightAfter, _) = serviceImage
        mListImageService.clear()

        setImageView(
            frontBefore,
            iv_image_front_before,
            iv_camera_front_before,
            card_remove_front_before,
            progress_bar_front_before
        )
        setImageView(
            backBefore,
            iv_image_back_before,
            iv_camera_back_before,
            card_remove_back_before,
            progress_bar_back_before
        )
        setImageView(
            leftBefore,
            iv_image_left_before,
            iv_camera_left_before,
            card_remove_left_before,
            progress_bar_left_before
        )
        setImageView(
            rightBefore,
            iv_image_right_before,
            iv_camera_right_before,
            card_remove_right_before,
            progress_bar_right_before
        )
        setImageView(
            frontAfter,
            iv_image_front_after,
            iv_camera_front_after,
            card_remove_front_after,
            progress_bar_front_after
        )
        setImageView(
            backAfter,
            iv_image_back_after,
            iv_camera_back_after,
            card_remove_back_after,
            progress_bar_back_after
        )
        setImageView(
            leftAfter,
            iv_image_left_after,
            iv_camera_left_after,
            card_remove_left_after,
            progress_bar_left_after
        )
        setImageView(
            rightAfter,
            iv_image_right_after,
            iv_camera_right_after,
            card_remove_right_after,
            progress_bar_right_after
        )
    }

    private fun setImageView(
        url: String?,
        ivImage: ImageView,
        ivCamera: ImageView,
        cardRemove: CardView,
        progress_bar: ProgressBar
    ) {
        progress_bar.hide()
        if (url != null) {
            mListImageService.add(1)
            ivCamera.hide()
            ivImage.setImageFromInternet(url)
            cardRemove.show()
        } else {
            ivCamera.show()
            ivImage.setImageResource(0)
            cardRemove.hide()
        }
    }

}
