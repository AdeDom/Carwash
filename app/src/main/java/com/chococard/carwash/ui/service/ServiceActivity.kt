package com.chococard.carwash.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ServiceViewModel
import kotlinx.android.synthetic.main.activity_service.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceActivity : BaseActivity() {

    val viewModel by viewModel<ServiceViewModel>()

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

        bt_service.setOnClickListener { startActivity<PaymentActivity>() }

        // observe
        viewModel.state.observe { state ->
            // progress bar
            if (state.loading) progress_bar.show() else progress_bar.hide()
            if (state.loadingFrontBefore) progress_bar_front_before.show() else progress_bar_front_before.hide()
            if (state.loadingBackBefore) progress_bar_back_before.show() else progress_bar_back_before.hide()
            if (state.loadingLeftBefore) progress_bar_left_before.show() else progress_bar_left_before.hide()
            if (state.loadingRightBefore) progress_bar_right_before.show() else progress_bar_right_before.hide()
            if (state.loadingFrontAfter) progress_bar_front_after.show() else progress_bar_front_after.hide()
            if (state.loadingBackAfter) progress_bar_back_after.show() else progress_bar_back_after.hide()
            if (state.loadingLeftAfter) progress_bar_left_after.show() else progress_bar_left_after.hide()
            if (state.loadingRightAfter) progress_bar_right_after.show() else progress_bar_right_after.hide()
            if (state.loadingOtherImage) progress_bar_other_image.show() else progress_bar_other_image.hide()

            // service image
            adt.setList(state.serviceImage?.otherImageService)

            // other image
            if (state.isValidMaximumOtherImage) {
                card_add_other_image.show()
                tv_maximum_other_image.hide()
            } else {
                card_add_other_image.hide()
                tv_maximum_other_image.show()
            }

            // button service
            if (state.isConfirmService) bt_service.ready() else bt_service.unready()

            // image service 8 image (camera)
            if (state.isImageFrontBefore) iv_camera_front_before.hide() else iv_camera_front_before.show()
            if (state.isImageBackBefore) iv_camera_back_before.hide() else iv_camera_back_before.show()
            if (state.isImageLeftBefore) iv_camera_left_before.hide() else iv_camera_left_before.show()
            if (state.isImageRightBefore) iv_camera_right_before.hide() else iv_camera_right_before.show()
            if (state.isImageFrontAfter) iv_camera_front_after.hide() else iv_camera_front_after.show()
            if (state.isImageBackAfter) iv_camera_back_after.hide() else iv_camera_back_after.show()
            if (state.isImageLeftAfter) iv_camera_left_after.hide() else iv_camera_left_after.show()
            if (state.isImageRightAfter) iv_camera_right_after.hide() else iv_camera_right_after.show()

            // image service 8 image (remove)
            if (state.isImageFrontBefore) card_remove_front_before.show() else card_remove_front_before.hide()
            if (state.isImageBackBefore) card_remove_back_before.show() else card_remove_back_before.hide()
            if (state.isImageLeftBefore) card_remove_left_before.show() else card_remove_left_before.hide()
            if (state.isImageRightBefore) card_remove_right_before.show() else card_remove_right_before.hide()
            if (state.isImageFrontAfter) card_remove_front_after.show() else card_remove_front_after.hide()
            if (state.isImageBackAfter) card_remove_back_after.show() else card_remove_back_after.hide()
            if (state.isImageLeftAfter) card_remove_left_after.show() else card_remove_left_after.hide()
            if (state.isImageRightAfter) card_remove_right_after.show() else card_remove_right_after.hide()

            // image service 8 image (Glide load image)
            val serviceImage = state.serviceImage
            if (state.isImageFrontBefore) iv_image_front_before.setImageFromInternet(serviceImage?.frontBefore) else iv_image_front_before.setImageResource(0)
            if (state.isImageBackBefore) iv_image_back_before.setImageFromInternet(serviceImage?.backBefore) else iv_image_back_before.setImageResource(0)
            if (state.isImageLeftBefore) iv_image_left_before.setImageFromInternet(serviceImage?.leftBefore) else iv_image_left_before.setImageResource(0)
            if (state.isImageRightBefore) iv_image_right_before.setImageFromInternet(serviceImage?.rightBefore) else iv_image_right_before.setImageResource(0)
            if (state.isImageFrontAfter) iv_image_front_after.setImageFromInternet(serviceImage?.frontAfter) else iv_image_front_after.setImageResource(0)
            if (state.isImageBackAfter) iv_image_back_after.setImageFromInternet(serviceImage?.backAfter) else iv_image_back_after.setImageResource(0)
            if (state.isImageLeftAfter) iv_image_left_after.setImageFromInternet(serviceImage?.leftAfter) else iv_image_left_after.setImageResource(0)
            if (state.isImageRightAfter) iv_image_right_after.setImageFromInternet(serviceImage?.rightAfter) else iv_image_right_after.setImageResource(0)
        }

        viewModel.attachFirstTime.observe {
            viewModel.callFetchImageService()
        }

        viewModel.error.observeError()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        menu?.findItem(R.id.option_report)?.isVisible = false
        return true
    }

    private fun deleteImageService(statusService: Int): Boolean {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            viewModel.callDeleteServiceImage(statusService)
        }
        return true
    }

    private fun deleteOtherImageService(imageId: Int) {
        dialogNegative(R.string.delete_image_service, R.string.delete_image_service_questions) {
            val deleteImageService = DeleteImageServiceRequest(imageId)
            viewModel.callDeleteServiceOtherImage(deleteImageService)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val statusService: Int = when (requestCode) {
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_BEFORE ->
                    FlagConstant.STATUS_SERVICE_FRONT_BEFORE
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_BEFORE ->
                    FlagConstant.STATUS_SERVICE_BACK_BEFORE
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_BEFORE ->
                    FlagConstant.STATUS_SERVICE_LEFT_BEFORE
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_BEFORE ->
                    FlagConstant.STATUS_SERVICE_RIGHT_BEFORE
                CommonsConstant.REQUEST_CODE_IMAGE_FRONT_AFTER ->
                    FlagConstant.STATUS_SERVICE_FRONT_AFTER
                CommonsConstant.REQUEST_CODE_IMAGE_BACK_AFTER ->
                    FlagConstant.STATUS_SERVICE_BACK_AFTER
                CommonsConstant.REQUEST_CODE_IMAGE_LEFT_AFTER ->
                    FlagConstant.STATUS_SERVICE_LEFT_AFTER
                CommonsConstant.REQUEST_CODE_IMAGE_RIGHT_AFTER ->
                    FlagConstant.STATUS_SERVICE_RIGHT_AFTER
                CommonsConstant.REQUEST_CODE_IMAGE_OTHER_IMAGE ->
                    FlagConstant.STATUS_SERVICE_OTHER_IMAGE
                else -> 0
            }

            viewModel.callUploadImageService(
                convertToMultipartBody(data.data!!),
                statusService
            )
        }
    }

}
