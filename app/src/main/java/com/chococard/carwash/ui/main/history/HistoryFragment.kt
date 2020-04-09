package com.chococard.carwash.ui.main.history

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.util.BaseFragment
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment<HistoryViewModel>({ R.layout.fragment_history }) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        fab.setOnClickListener {
            context?.toast("hello")
        }
    }

}
