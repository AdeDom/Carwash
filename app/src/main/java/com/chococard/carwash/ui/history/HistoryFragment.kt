package com.chococard.carwash.ui.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.models.DateRangePicker
import com.chococard.carwash.ui.DateRangePickerActivity
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.ui.historydetail.HistoryDetailActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : BaseFragment(R.layout.fragment_history) {

    val viewModel by viewModel<HistoryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adt = HistoryAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adt
        }

        // observe
        viewModel.state.observe { state ->
            // progress bar
            if (state.loading) progress_bar.show() else progress_bar.hide()

            if (state.histories?.isEmpty() == true) layout_not_found.show() else layout_not_found.hide()
            adt.setList(state.histories)
        }

        viewModel.error.observeError()

        // event
        adt.onClick = {
            Intent(context, HistoryDetailActivity::class.java).apply {
                putExtra(CommonsConstant.HISTORY, it)
                startActivity(this)
            }
        }

        fab.setOnClickListener {
            Intent(context, DateRangePickerActivity::class.java).apply {
                startActivityForResult(this, CommonsConstant.REQUEST_CODE_DATE_RANGE)
            }
        }

        // call api
        viewModel.callFetchHistory()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_DATE_RANGE && resultCode == Activity.RESULT_OK && data != null) {
            val (dateBegin, dateEnd) = data.getParcelableExtra<DateRangePicker>(CommonsConstant.DATE_RANGE_PICKER)!!
            if (dateBegin != null && dateEnd != null)
                viewModel.callFetchHistory(dateBegin, dateEnd)
        }
    }

}
