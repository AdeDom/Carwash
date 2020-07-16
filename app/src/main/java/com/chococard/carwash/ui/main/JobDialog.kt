package com.chococard.carwash.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.viewmodel.TimerJobViewModel
import kotlinx.android.synthetic.main.dialog_job.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class JobDialog(private val listener: FlagJobListener) : BaseDialog(R.layout.dialog_job) {

    val viewModel: TimerJobViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job? ?: return

        // set widgets
        val (_, employeeId, fullName, imageProfile, _, packageName, _, _, _, _, location, distance, _) = job
        tv_full_name.text = fullName
        tv_service.text = packageName
        tv_location.text = location
        tv_distance.text = distance
        iv_photo.setImageCircle(imageProfile)

        // observe
        viewModel.getTimerJobQuestion.observe(viewLifecycleOwner, Observer { response ->
            val (success, _, timer) = response
            if (success && employeeId == timer?.employeeId) {
                if (timer?.timer ?: 0 <= 0) dismiss()
                tv_count_time.text = timer?.timer.toString()
            }
        })

        // set event
        bt_cancel.setOnClickListener {
            listener.onFlag(FlagConstant.JOB_REJECT)
            dismiss()
        }
        bt_confirm.setOnClickListener {
            listener.onFlag(FlagConstant.JOB_RECEIVE)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startSignalRTimeHub()
    }

    override fun onPause() {
        viewModel.stopSignalRTimeHub()
        listener.onFlag(FlagConstant.JOB_REJECT)
        dismiss()
        super.onPause()
    }

    interface FlagJobListener {
        fun onFlag(flag: Int)
    }

}
