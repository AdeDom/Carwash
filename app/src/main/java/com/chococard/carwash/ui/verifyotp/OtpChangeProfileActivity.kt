package com.chococard.carwash.ui.verifyotp

import android.app.Activity
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_verify_otp.*

class OtpChangeProfileActivity : BaseVerifyOtpActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(credential)
            ?.addOnCompleteListener { task ->
                progress_bar.hide()
                if (task.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    root_layout.snackbar(task.exception?.message)
                }
            }
    }

}
