package com.chococard.carwash.ui.main

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.util.Commons
import com.chococard.carwash.util.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentDialog : BaseDialog(R.layout.dialog_payment) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        //event
        rl_dismiss.setOnClickListener { dismiss() }
        bt_report.setOnClickListener { report() }
        bt_confirm.setOnClickListener { payment() }
    }

    private fun report() {
        listener.onAttach(Commons.PAYMENT_REPORT)
        dismiss()
    }

    private fun payment() {
        listener.onAttach(Commons.PAYMENT_CONFIRM)
        dismiss()
    }

}
