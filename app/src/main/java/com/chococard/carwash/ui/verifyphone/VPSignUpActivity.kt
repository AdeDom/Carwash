package com.chococard.carwash.ui.verifyphone

import android.widget.Toast
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_verify_phone.*

class VPSignUpActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                progress_bar.hide()
                if (task.isSuccessful) {
                    startActivity<SignUpActivity> {
                        it.putExtra(CommonsConstant.PHONE, mPhoneNumber)
                        finish()
                    }
                } else {
                    toast(task.exception?.message, Toast.LENGTH_LONG)
                }
            }
    }

}
