package com.chococard.carwash.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.history.HistoryFragment
import com.chococard.carwash.ui.home.HomeFragment
import com.chococard.carwash.ui.payment.PaymentActivity
import com.chococard.carwash.ui.profile.ProfileFragment
import com.chococard.carwash.ui.wallet.WalletFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.JobFlag
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    FlagJobListener {

    val viewModel: MainViewModel by viewModel()

    private var mBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (readJobFlag() == JobFlag.JOB_FLAG_ON.toString()) {
            startActivity<PaymentActivity>()
        }

        setToolbar(toolbar)
        setReceiverLocation()

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(HomeFragment())

        bt_has_job.setOnClickListener {
            viewModel.callJobRequest()
        }

        //call api
        val logsKeys = UUID.randomUUID().toString().replace("-", "")
        writePref(CommonsConstant.LOGS_KEYS, logsKeys)
//        viewModel.callSetLogsActive(FlagConstant.LOGS_STATUS_ACTIVE, logsKeys)

        // fetch user info
        progress_bar.show()
        viewModel.callFetchUser()

        //observe
        viewModel.getUser.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message, _) = response
            if (!success) {
                finishAffinity()
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getLogsActive.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getJobRequest.observe(this, Observer { request ->
            val (success, message, jobRequest) = request
            if (success) {
                val bundle = Bundle()
                bundle.putParcelable(CommonsConstant.JOB, jobRequest)

                val jobDialog = JobDialog()
                jobDialog.arguments = bundle
                jobDialog.show(supportFragmentManager, null)
            } else {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getJobResponse.observe(this, Observer { response ->
            val (success, message, jobFlag) = response
            if (success) {
                if (jobFlag) {
                    writeJobFlag(JobFlag.JOB_FLAG_ON)
                    startActivity<PaymentActivity>()
                } else {
                    writeJobFlag(JobFlag.JOB_FLAG_OFF)
                }
            } else {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getLogout.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getError.observe(this, Observer {
            dialogError(it)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment())
            R.id.nav_wallet -> replaceFragment(WalletFragment())
            R.id.nav_history -> replaceFragment(HistoryFragment())
            R.id.nav_profile -> replaceFragment(ProfileFragment())
        }
        return true
    }

    private fun replaceFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                startActivity<ChangeProfileActivity>()
            }
            R.id.option_change_password -> {
                startActivity<ChangePasswordActivity>()
            }
            R.id.option_contact_admin -> dialogContactAdmin()
            R.id.option_logout -> dialogLogout {
                viewModel.deleteUser()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setReceiverLocation() {
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isGpsEnabled =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) //NETWORK_PROVIDER

                    if (!isGpsEnabled) {
                        settingLocation()
                    }
                }
            }
        }

        settingLocation()
    }

    override fun onResume() {
        super.onResume()
        //Register receiver.
        broadcastReceiver(true)
    }

    override fun onPause() {
        super.onPause()
        //Unregister receiver.
        broadcastReceiver(false)

        // set user logs active
        val logsKeys = readPref(CommonsConstant.LOGS_KEYS)
//        viewModel.callSetLogsActive(FlagConstant.LOGS_STATUS_INACTIVE, logsKeys)
    }

    // When location is not enabled, the application will end.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isLocationProviderEnabled = Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
        if (!isLocationProviderEnabled && requestCode == CommonsConstant.REQUEST_CODE_LOCATION) {
            finishAffinity()
        }
    }

    // Set up receiver register & unregister.
    private fun broadcastReceiver(isReceiver: Boolean) {
        if (isReceiver) {
            val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
            filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
            registerReceiver(mBroadcastReceiver, filter)
        } else {
            unregisterReceiver(mBroadcastReceiver)
        }
    }

    // If location off give on setting on.
    private fun settingLocation() {
        val isLocationProviderEnabled = Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
        if (!isLocationProviderEnabled) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                startActivityForResult(this, CommonsConstant.REQUEST_CODE_LOCATION)
            }
        }
    }

    override fun onFlag(flag: Int) = viewModel.callJobResponse(flag)

}
