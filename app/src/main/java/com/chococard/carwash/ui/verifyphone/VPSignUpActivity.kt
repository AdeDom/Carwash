package com.chococard.carwash.ui.verifyphone

import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.snackbar
import com.chococard.carwash.util.extension.startActivity
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
                    root_layout.snackbar(task.exception?.message)
                }
            }
    }

}
