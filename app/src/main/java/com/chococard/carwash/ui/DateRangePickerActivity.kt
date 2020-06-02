package com.chococard.carwash.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.util.Pair
import com.chococard.carwash.data.models.DateRangePicker
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class DateRangePickerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().apply {
            setSelection(Pair(Date().time, Date().time))
        }.build()

        dateRangePicker.show(supportFragmentManager, null)

        dateRangePicker.addOnPositiveButtonClickListener {
            val dateRange = DateRangePicker(it.first, it.second)
            val intent = Intent()
            intent.putExtra(CommonsConstant.DATE_RANGE_PICKER, dateRange)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        dateRangePicker.addOnDismissListener {
            finish()
        }
    }

}
