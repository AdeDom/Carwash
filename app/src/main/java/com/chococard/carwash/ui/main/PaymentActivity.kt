package com.chococard.carwash.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chococard.carwash.R
import com.chococard.carwash.data.models.Job
import com.chococard.carwash.util.extension.toast

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val job = intent.getParcelableExtra(getString(R.string.job)) as Job

        toast(job.toString())
    }

}
