package com.chococard.carwash.ui.verifyphone

import android.widget.Toast
import com.chococard.carwash.R
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class VPSignInActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    task.exception?.message?.let { toast(it, Toast.LENGTH_LONG) }
                }
            }
    }

    override fun onBackPressed() = toast(getString(R.string.not_available), Toast.LENGTH_LONG)

}
