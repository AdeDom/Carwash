package com.chococard.carwash.ui.history

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.BaseDialog
import com.chococard.carwash.util.extension.dialogDatePicker
import kotlinx.android.synthetic.main.dialog_filter_date.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryDialog(
    private val listener: FilterDateListener
) : BaseDialog(R.layout.dialog_filter_date, true) {

    private lateinit var mDateBegin: String
    private lateinit var mDateEnd: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        // initial values
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = sdf.format(Date())
        mDateBegin = "0000/00/00"
        mDateEnd = date
        tv_date_end.text = date

        // event
        iv_date_begin.setOnClickListener { showDateBegin() }
        tv_date_begin.setOnClickListener { showDateBegin() }
        iv_date_end.setOnClickListener { showDateEnd() }
        tv_date_end.setOnClickListener { showDateEnd() }
        bt_filter_date.setOnClickListener { filterDay() }
    }

    private fun showDateBegin() = activity?.dialogDatePicker { date ->
        val (year, mount, day) = date
        mDateBegin = "$year/$mount/$day"
        tv_date_begin.text = mDateBegin
    }

    private fun showDateEnd() = activity?.dialogDatePicker { date ->
        val (year, mount, day) = date
        mDateEnd = "$year/$mount/$day"
        tv_date_end.text = mDateEnd
    }

    private fun filterDay() {
        listener.onFilterDate(mDateBegin, mDateEnd)
        dismiss()
    }

}
