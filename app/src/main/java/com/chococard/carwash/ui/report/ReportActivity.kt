package com.chococard.carwash.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ReportRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ReportViewModel
import kotlinx.android.synthetic.main.activity_report.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportActivity : BaseActivity() {

    val viewModel: ReportViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        init()
    }

    private fun init() {
        // set toolbar
        setToolbar(toolbar)

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        root_layout.setOnClickListener { hideSoftKeyboard() }

        bt_report.setOnClickListener { reportJob() }

        // observe
        viewModel.getReportJob.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun reportJob() {
        val report = et_report.getContents()

        when {
            report.isEmpty() -> {
                dialogError(getString(R.string.error_empty_report))
                return
            }
        }

        progress_bar.show()
        viewModel.callReportJob(ReportRequest(report))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option_busy, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_contact_admin -> startActivityActionDial()
        }
        return super.onOptionsItemSelected(item)
    }

}
