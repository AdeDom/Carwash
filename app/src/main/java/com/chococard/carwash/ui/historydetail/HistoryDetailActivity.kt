package com.chococard.carwash.ui.historydetail

import android.os.Bundle
import android.util.Log
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant

class HistoryDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        val history = intent.getParcelableExtra<History>(CommonsConstant.HISTORY)
        Log.d(TAG, "onCreate: $history")
    }

    companion object {
        private const val TAG = "HistoryDetailActivity"
    }

}
