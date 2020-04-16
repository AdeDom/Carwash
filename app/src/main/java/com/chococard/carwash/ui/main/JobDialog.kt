package com.chococard.carwash.ui.main

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.models.Job
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseDialog
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.loadCircle
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.dialog_job.*

class JobDialog : BaseDialog<MainViewModel, MainFactory>(R.layout.dialog_job) {

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

        // set event
        bt_cancel.setOnClickListener { rejectJob() }
        bt_confirm.setOnClickListener { receiveJob() }

    }

    private fun receiveJob() {
        context?.toast("Receive Job")
        dialog?.dismiss()
    }

    private fun rejectJob() {
        dialog?.dismiss()
    }

}
