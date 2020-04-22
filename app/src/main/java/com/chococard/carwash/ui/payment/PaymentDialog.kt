package com.chococard.carwash.ui.payment

import android.content.Context
import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.ui.OnAttachListener
import com.chococard.carwash.util.FlagConstant
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentDialog : BaseDialog(R.layout.dialog_payment) {

    private lateinit var listener: OnAttachListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAttachListener
    }

    private fun init() {
        //event
        rl_dismiss.setOnClickListener { dismiss() }
        bt_report.setOnClickListener { report() }
        bt_confirm.setOnClickListener { payment() }
    }

    private fun report() {
        listener.onAttach(FlagConstant.PAYMENT_REPORT)
        dismiss()
    }

    private fun payment() {
        listener.onAttach(FlagConstant.PAYMENT_CONFIRM)
        dismiss()
    }

}
