package com.chococard.carwash.ui.verifyphone

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_verify_phone.*
import java.util.concurrent.TimeUnit

abstract class BaseVerifyPhoneActivity : BaseActivity() {

    private var mIsReSendMessage = true
    private var mVerificationId: String? = null
    protected var mPhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        init()
    }

    private fun init() {
        //get intent phone
        mPhoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (mPhoneNumber == null) finish() else requestOtp()

        //set event
        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_verify_otp.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) verifyPhone()
            false
        }

        bt_re_send_message.setOnClickListener {
            if (mIsReSendMessage) {
                mIsReSendMessage = false
                requestOtp()
            } else {
                toast(
                    getString(R.string.please_check_message_phone_number, mPhoneNumber),
                    Toast.LENGTH_LONG
                )
            }
        }

        bt_verify_phone.setOnClickListener {
            verifyPhone()
        }
    }

    private fun verifyPhone() {
        when {
            et_verify_otp.isEmpty(getString(R.string.error_empty_otp)) -> return
            et_verify_otp.isEqualLength(6, getString(R.string.error_equal_length, 6)) -> return
            mVerificationId != null -> {
                progress_bar.show()
                val smsCode = et_verify_otp.getContents()
                val credential = PhoneAuthProvider.getCredential(mVerificationId!!, smsCode)
                firebaseAuthPhone(credential)
            }
            else -> toast(getString(R.string.please_re_send_message), Toast.LENGTH_LONG)
        }
    }

    private fun requestOtp() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                et_verify_otp.setText(credential.smsCode)
                firebaseAuthPhone(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                toast(e.message, Toast.LENGTH_LONG)
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
