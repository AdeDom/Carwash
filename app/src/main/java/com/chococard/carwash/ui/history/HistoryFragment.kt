package com.chococard.carwash.ui.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.DateRangePicker
import com.chococard.carwash.ui.DateRangePickerActivity
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    val viewModel: HistoryViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        val adt = HistoryAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adt
        }

        fab.setOnClickListener {
            Intent(context, DateRangePickerActivity::class.java).apply {
                startActivityForResult(this, CommonsConstant.REQUEST_CODE_DATE_RANGE)
            }
        }

        // call api
        progress_bar.show()
        viewModel.callFetchHistory()

        // observe
        viewModel.getHistory.observe(viewLifecycleOwner, Observer { response ->
            val (success, message, listHistory) = response
            progress_bar.hide()
            if (success) {
                listHistory?.let { adt.setList(it) }
            } else {
                message?.let { context.toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getError.observe(viewLifecycleOwner, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun filterDate() = activity?.supportFragmentManager?.let { fragmentManager ->
        HistoryDialog(object : FilterDateListener {
            override fun onFilterDate(dateBegin: String, dateEnd: String) {
                progress_bar.show()
                viewModel.callFetchHistory(dateBegin, dateEnd)
            }
        }).show(fragmentManager, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_DATE_RANGE && resultCode == Activity.RESULT_OK && data != null) {
            val dateRange = data.getParcelableExtra<DateRangePicker>(CommonsConstant.DATE_RANGE_PICKER)
            context.toast(dateRange.toString(), Toast.LENGTH_LONG)
        }
    }

}
