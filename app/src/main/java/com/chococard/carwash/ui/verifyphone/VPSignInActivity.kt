package com.chococard.carwash.ui.verifyphone

import com.chococard.carwash.R
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.snackbar
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
                    root_layout.snackbar(task.exception?.message)
                }
            }
    }

    override fun onBackPressed() = root_layout.snackbar(getString(R.string.not_available))

}
