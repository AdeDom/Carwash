package com.chococard.carwash.ui.payment

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.FlagConstant
import kotlinx.android.synthetic.main.dialog_payment.*

class PaymentDialog : BaseDialog(R.layout.dialog_payment) {

    private lateinit var listener: FlagPaymentListener
    private var mFlagPayment: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        listener = context as FlagPaymentListener

        //event
        rl_dismiss.setOnClickListener { dialog?.hide() }
        bt_report.setOnClickListener { report() }
        bt_confirm.setOnClickListener { payment() }
    }

    private fun report() {
        mFlagPayment = FlagConstant.PAYMENT_REPORT
        dismiss()
    }

    private fun payment() {
        mFlagPayment = FlagConstant.PAYMENT_CONFIRM
        dismiss()
    }

    override fun dismiss() {
        listener.onFlag(mFlagPayment)
        super.dismiss()
    }

}
