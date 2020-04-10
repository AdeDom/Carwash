package com.chococard.carwash.ui.main.history

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.HistoryApi
import com.chococard.carwash.data.repositories.HistoryRepository
import com.chococard.carwash.util.BaseFragment
import com.chococard.carwash.util.extension.dialogDatePicker
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment<HistoryViewModel>({ R.layout.fragment_history }) {

    private lateinit var mDateBegin: String
    private lateinit var mDateEnd: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val factory =
            HistoryFactory(HistoryRepository(HistoryApi.invoke(networkConnectionInterceptor)))
        viewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)

        fab.setOnClickListener {
            activity?.dialogDatePicker { year, month, dayOfMonth ->
                mDateBegin = "$dayOfMonth/$month/$year"

                activity?.dialogDatePicker { year, month, dayOfMonth ->
                    mDateEnd = "$dayOfMonth/$month/$year"

                    val mDateBetween = "$mDateBegin - $mDateEnd"
                    context?.toast(mDateBetween)

                    progress_bar.show()
                    viewModel.fetchHistory(mDateBegin, mDateEnd)
                }
            }
        }

        // call api
        progress_bar.show()
        viewModel.fetchHistory("", "")

        // observe
        viewModel.history.observe(viewLifecycleOwner, Observer { response ->
            progress_bar.hide()
            if (response.success) {
                context?.toast(response.history.toString())
            } else {
                response.message?.let { context?.toast(it) }
            }
        })

        // exception
        viewModel.exception = {
            progress_bar.hide()
            context?.toast(it)
        }
    }

}
