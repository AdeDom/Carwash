package com.chococard.carwash.ui.verifyphone

import android.widget.Toast
import com.chococard.carwash.R
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_verify_phone.*

class VPSignInActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                progress_bar.hide()
                if (task.isSuccessful) {
                    finish()
                } else {
                    toast(task.exception?.message, Toast.LENGTH_LONG)
                }
            }
    }

    override fun onBackPressed() = toast(getString(R.string.not_available), Toast.LENGTH_LONG)

}
