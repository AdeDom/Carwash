package com.chococard.carwash.ui.verifyotp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_verify_otp.*
import java.util.concurrent.TimeUnit

abstract class BaseVerifyOtpActivity : BaseActivity() {

    private var mIsReSendMessage = true
    private var mVerificationId: String? = null
    protected var mPhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        init()
    }

    private fun init() {
        //get intent phone
        mPhoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (mPhoneNumber == null) finish() else requestOtp()

        // set widget button verify otp
        validateOtp(et_verify_otp.getContents())

        //set event
        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_verify_otp.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) verifyOtp()
            false
        }

        bt_re_send_message.setOnClickListener {
            if (mIsReSendMessage) {
                mIsReSendMessage = false
                requestOtp()
            } else {
                root_layout.snackbar(
                    getString(R.string.please_check_message_phone_number, mPhoneNumber)
                )
            }
        }

        bt_verify_otp.setOnClickListener {
            verifyOtp()
        }

        et_verify_otp.addTextChangedListener { validateOtp(it.toString()) }
    }

    private fun validateOtp(otp: String) {
        when {
            otp.isEmpty() -> bt_verify_otp.unready()
            otp.length != 6 -> bt_verify_otp.unready()
            else -> bt_verify_otp.ready()
        }
    }

    private fun verifyOtp() {
        val otp = et_verify_otp.getContents()

        when {
            otp.isEmpty() -> root_layout.snackbar(getString(R.string.error_empty_otp))
            otp.length != 6 -> root_layout.snackbar(getString(R.string.error_equal_length, 6))
            mVerificationId != null -> {
                progress_bar.show()
                val smsCode = et_verify_otp.getContents()
                val credential = PhoneAuthProvider.getCredential(mVerificationId!!, smsCode)
                firebaseAuthPhone(credential)
            }
            else -> root_layout.snackbar(getString(R.string.please_re_send_message))
        }
    }

    private fun requestOtp() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                et_verify_otp.setText(credential.smsCode)
                firebaseAuthPhone(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                root_layout.snackbar(e.message)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                mVerificationId = verificationId
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+66${mPhoneNumber?.substring(1, 10)}",
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    abstract fun firebaseAuthPhone(credential: PhoneAuthCredential)

}
