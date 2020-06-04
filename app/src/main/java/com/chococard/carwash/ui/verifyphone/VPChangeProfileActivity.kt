package com.chococard.carwash.ui.verifyphone

import android.app.Activity
import android.widget.Toast
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential

class VPChangeProfileActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    toast(task.exception?.message, Toast.LENGTH_LONG)
                }
            }
    }

}
