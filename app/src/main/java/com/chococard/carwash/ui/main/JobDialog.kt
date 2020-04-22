package com.chococard.carwash.ui.main

import android.content.Context
import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.models.Job
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.ui.OnAttachListener
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.loadCircle
import kotlinx.android.synthetic.main.dialog_job.*
import kotlinx.coroutines.delay

class JobDialog : BaseDialog(R.layout.dialog_job) {

    private lateinit var listener: OnAttachListener
    private var mIsTimer: Boolean = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAttachListener
    }

    private fun init() {
        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job?

        // set widgets
        val (_, fullName, image, _, _, service, _, _, _, endLat, endLong, _, _, _) = job as Job
        tv_full_name.text = fullName
        tv_service.text = service
        if (endLat != null && endLong != null)
            tv_location.text = context?.getLocality(endLat, endLong)
        image?.let { iv_photo.loadCircle(it) }

        setCountTime()

        // set event
        bt_cancel.setOnClickListener { rejectJob() }
        bt_confirm.setOnClickListener { receiveJob() }
    }

    private fun setCountTime() {
        Coroutines.main {
            for (t in 0..14) {
                if (mIsTimer) {
                    val time = 15 - t
                    tv_count_time.text = time.toString()
                    delay(1000)
                } else {
                    return@main
                }
            }

            listener.onAttach(FlagConstant.JOB_REJECT)
            dismiss()
        }
    }

    private fun receiveJob() {
        mIsTimer = false
        listener.onAttach(FlagConstant.JOB_RECEIVE)
        dismiss()
    }

    private fun rejectJob() {
        mIsTimer = false
        listener.onAttach(FlagConstant.JOB_REJECT)
        dismiss()
    }

}
