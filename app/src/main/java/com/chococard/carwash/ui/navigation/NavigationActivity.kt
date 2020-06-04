package com.chococard.carwash.ui.navigation

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.viewmodel.NavigationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationActivity : BaseActivity() {

    val viewModel: NavigationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        viewModel.getJob.observe(this, Observer { job ->
            if (job == null) return@Observer
            Log.d(TAG, "onCreate: $job")
        })
    }

    companion object {
        private const val TAG = "NavigationActivity"
    }

}
