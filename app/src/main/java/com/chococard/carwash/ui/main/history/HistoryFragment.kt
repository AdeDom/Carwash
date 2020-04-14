package com.chococard.carwash.ui.main.history

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseFragment
import com.chococard.carwash.util.extension.dialogDatePicker
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment<HistoryViewModel, HistoryFactory>(R.layout.fragment_history) {

    override fun viewModel() = HistoryViewModel::class.java

    override fun factory() = HistoryFactory(MainRepository(MainApi.invoke(requireContext())))

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
            activity?.dialogDatePicker { begin ->
                val (bDayOfMonth, bMonth, bYear) = begin
                val dateBegin = "$bDayOfMonth/$bMonth/$bYear"

                activity?.dialogDatePicker { end ->
                    val (eDayOfMonth, eMonth, eYear) = end
                    val dateEnd = "$eDayOfMonth/$eMonth/$eYear"

                    progress_bar.show()
                    viewModel.fetchHistory(dateBegin, dateEnd)
                }
            }
        }

        // call api
        progress_bar.show()
        viewModel.fetchHistory()

        // observe
        viewModel.history.observe(viewLifecycleOwner, Observer { response ->
            val (success, message, listHistory) = response
            progress_bar.hide()
            if (success) {
                listHistory?.let { adt.setList(it) }
            } else {
                message?.let { context?.toast(it) }
            }
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer {
            progress_bar.hide()
            context?.toast(it)
        })
    }

}
