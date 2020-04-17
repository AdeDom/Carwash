package com.chococard.carwash.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.Job
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.base.BaseDialog
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.loadCircle
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.util.extension.writePref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog_job.*
import kotlinx.coroutines.delay

class JobDialog : BaseDialog<MainViewModel, MainFactory>(R.layout.dialog_job) {

    private var mIsTimer: Boolean = true

    override fun viewModel() = MainViewModel::class.java

    override fun factory() = MainFactory(MainRepository(MainApi.invoke(requireContext())))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val job = arguments?.getParcelable(getString(R.string.job)) as Job?

        // set widgets
        val (_, fullName, image, _, _, service, _, latitude, longitude, _, _, _) = job as Job
        tv_full_name.text = fullName
        tv_service.text = service
        if (latitude != null && longitude != null)
            tv_location.text = context?.getLocality(latitude, longitude)
        image?.let { iv_photo.loadCircle(it) }

        setCountTime()

        // set event
        bt_cancel.setOnClickListener { rejectJob() }
        bt_confirm.setOnClickListener { receiveJob() }

        viewModel.jobResponse.observe(viewLifecycleOwner, Observer { response ->
            val (success, message, jobFlag) = response
            if (success) {
                if (jobFlag) {
                    context?.writePref(R.string.flag, "1")
                    context?.writePref(R.string.job, Gson().toJson(job))
                    Intent(context, PaymentActivity::class.java).apply {
                        putExtra(getString(R.string.job), job)
                        startActivity(this)
                    }
                } else {
                    context?.writePref(R.string.flag, "0")
                }
            } else {
                message?.let { context?.toast(it) }
            }
            dialog?.dismiss()
        })

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

            viewModel.jobResponse(getString(R.string.reject))
        }
    }

    private fun receiveJob() {
        mIsTimer = false
        viewModel.jobResponse(getString(R.string.receive))
    }

    private fun rejectJob() {
        mIsTimer = false
        viewModel.jobResponse(getString(R.string.reject))
    }

}
