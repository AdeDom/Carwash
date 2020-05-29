package com.chococard.carwash.ui.verifyphone

import android.widget.Toast
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class VPSignUpActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity<SignUpActivity> {
                        it.putExtra(CommonsConstant.PHONE, mPhoneNumber)
                        finish()
                    }
                } else {
                    task.exception?.message?.let { toast(it, Toast.LENGTH_LONG) }
                }
            }
    }

}
