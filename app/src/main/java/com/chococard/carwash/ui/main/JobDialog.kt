package com.chococard.carwash.ui.main

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.setImageCircle
import kotlinx.android.synthetic.main.dialog_job.*
import kotlinx.coroutines.delay

class JobDialog : BaseDialog(R.layout.dialog_job) {

    private lateinit var listener: FlagJobListener
    private var mFlagJob: Int = 0
    private var mIsTimer: Boolean = true

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        listener = context as FlagJobListener

        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job?

        // set widgets
        val (_, fullName, image, _, _, service, _, _, _, endLat, endLong, _, _, _) = job as Job
        tv_full_name.text = fullName
        tv_service.text = service
        if (endLat != null && endLong != null)
            tv_location.text = context?.getLocality(endLat, endLong)
        image?.let { iv_photo.setImageCircle(it) }

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

            rejectJob()
        }
    }

    private fun receiveJob() {
        mIsTimer = false
        mFlagJob = FlagConstant.JOB_RECEIVE
        dismiss()
    }

    private fun rejectJob() {
        mIsTimer = false
        mFlagJob = FlagConstant.JOB_REJECT
        dismiss()
    }

    override fun dismiss() {
        listener.onFlag(mFlagJob)
        super.dismiss()
    }

}
