package com.chococard.carwash.ui.verifyphone

import android.app.Activity
import android.widget.Toast
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_verify_phone.*

class VPChangeProfileActivity : BaseVerifyPhoneActivity() {

    override fun firebaseAuthPhone(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.updatePhoneNumber(credential)
            ?.addOnCompleteListener { task ->
                progress_bar.hide()
                if (task.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    toast(task.exception?.message, Toast.LENGTH_LONG)
                }
            }
    }

}
