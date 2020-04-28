package com.chococard.carwash.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.HistoryFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : BaseFragment<HistoryViewModel, HistoryFactory>(R.layout.fragment_history) {

    override fun viewModel() = HistoryViewModel::class.java

    override fun factory() = HistoryFactory(repositoryHeader)

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

        fab.setOnClickListener { filterDate() }

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

}
