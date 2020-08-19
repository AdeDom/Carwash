package com.chococard.carwash.ui.main

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.setImageCircle
import kotlinx.android.synthetic.main.dialog_job.*
import kotlinx.coroutines.delay

class JobDialog(private val listener: FlagJobListener) : BaseDialog(R.layout.dialog_job) {

    private var mTimerDialog: Int = 15
    private var mIsOnPause = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job? ?: return

        // set widgets
        val (_, _, fullName, imageProfile, _, packageName, _, _, _, _, location, distance, _) = job
        tv_full_name.text = fullName
        tv_service.text = packageName
        tv_location.text = location
        tv_distance.text = distance
        iv_photo.setImageCircle(imageProfile)

        setTimerDialog()

        // set event
        bt_cancel.setOnClickListener { jobAnswer(FlagConstant.JOB_REJECT) }
        bt_confirm.setOnClickListener { jobAnswer(FlagConstant.JOB_RECEIVE) }
    }

    private fun setTimerDialog() {
        Coroutines.main {
            while (mTimerDialog >= 0) {
                // job answer
                when {
                    mTimerDialog <= 0 && !mIsOnPause -> jobAnswer(FlagConstant.JOB_TIME_OUT)
                    mIsOnPause -> jobAnswer(FlagConstant.JOB_REJECT)
                }

                // sound
                MediaPlayer.create(context, R.raw.sound_bell).start()

                // vibrate
                val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                else
                    vibrator.vibrate(500)

                // set text timer
                tv_count_time.text = mTimerDialog.toString()

                mTimerDialog--
                delay(1_000)
            }
        }
    }

    private fun jobAnswer(flag: Int) {
        mTimerDialog = -1
        listener.onFlag(flag)
        if (!mIsOnPause) dismiss()
    }

    override fun onResume() {
        super.onResume()
        if (mTimerDialog <= -1) dismiss()
    }

    override fun onPause() {
        mIsOnPause = true
        super.onPause()
    }

    interface FlagJobListener {
        fun onFlag(flag: Int)
    }

}
