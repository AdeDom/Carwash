package com.chococard.carwash.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.dialog_job.*
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class JobDialog(private val listener: FlagJobListener) : BaseDialog(R.layout.dialog_job) {

    val viewModel: MainViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val job = arguments?.getParcelable(CommonsConstant.JOB) as Job? ?: return

        // set widgets
        val (_, fullName, imageProfile, _, packageName, _, _, latitude, longitude, distance, _) = job
        tv_full_name.text = fullName
        tv_service.text = packageName
        if (latitude != null && longitude != null)
            tv_location.text = context?.getLocality(latitude, longitude)
        tv_distance.text = distance
        iv_photo.setImageCircle(imageProfile)

        // observe
        viewModel.getCountTime.observe(viewLifecycleOwner, Observer {
            tv_count_time.text = it.toString()
        })

        viewModel.getJobFlag.observe(viewLifecycleOwner, Observer {
            listener.onFlag(it)
            dismiss()
        })

        setCountTime()

        // set event
        bt_cancel.setOnClickListener {
            viewModel.setValueJobFlag(FlagConstant.JOB_REJECT)
        }
        bt_confirm.setOnClickListener {
            viewModel.setValueJobFlag(FlagConstant.JOB_RECEIVE)
        }
    }

    private fun setCountTime() {
        Coroutines.main {
            var isTime = true
            var time: Int = CommonsConstant.MAX_COUNT_TIME
            while (isTime) {
                isTime = time > 0
                viewModel.setValueCountTime(time--)
                delay(1000)
            }
            viewModel.setValueJobFlag(FlagConstant.JOB_REJECT_TIME_OUT)
        }
    }

}
