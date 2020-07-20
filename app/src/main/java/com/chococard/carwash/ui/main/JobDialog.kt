package com.chococard.carwash.ui.main

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.models.Timer
import com.chococard.carwash.data.networks.request.JobAnswerRequest
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.setImageCircle
import kotlinx.android.synthetic.main.dialog_job.*

class JobDialog(private val listener: FlagJobListener) : BaseDialog(R.layout.dialog_job) {

    private var mJobId: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job? ?: return

        // set widgets
        val (jobId, employeeId, fullName, imageProfile, _, packageName, _, _, _, _, location, distance, _) = job
        mJobId = jobId
        tv_full_name.text = fullName
        tv_service.text = packageName
        tv_location.text = location
        tv_distance.text = distance
        iv_photo.setImageCircle(imageProfile)

        // TODO: 20/07/2563 for loop time

        // set event
        bt_cancel.setOnClickListener { jobAnswer(FlagConstant.JOB_REJECT) }
        bt_confirm.setOnClickListener { jobAnswer(FlagConstant.JOB_RECEIVE) }
    }

    private fun onAlertTimer(timer: Timer?) {
        // job answer
        if (timer?.timer ?: 0 <= 0) jobAnswer(FlagConstant.JOB_TIME_OUT)

        // sound
        MediaPlayer.create(context, R.raw.sound_bell).start()

        // vibrate
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        else
            vibrator.vibrate(500)

        // set text timer
        tv_count_time.text = timer?.timer.toString()
    }

    private fun jobAnswer(flag: Int) {
        listener.onFlag(JobAnswerRequest(mJobId, flag))
        dismiss()
    }

    override fun onPause() {
        jobAnswer(FlagConstant.JOB_REJECT)
        super.onPause()
    }

    interface FlagJobListener {
        fun onFlag(answer: JobAnswerRequest)
    }

}
