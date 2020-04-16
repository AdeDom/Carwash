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
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.ui.change.ChangePasswordActivity
import com.chococard.carwash.ui.change.ChangeProfileActivity
import com.chococard.carwash.ui.main.history.HistoryFragment
import com.chococard.carwash.ui.main.map.MapFragment
import com.chococard.carwash.ui.main.wallet.WalletFragment
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel, MainFactory>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var mBroadcastReceiver: BroadcastReceiver? = null

    override fun viewModel() = MainViewModel::class.java

    override fun factory() = MainFactory(MainRepository(MainApi.invoke(baseContext)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(toolbar)
        setReceiverLocation()

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(MapFragment())

        bt_has_job.setOnClickListener {
            viewModel.jobRequest()
        }

        viewModel.jobRequest.observe(this, Observer { request ->
            val (success, message, job) = request

            if (success) {
                val bundle = Bundle()
                bundle.putParcelable(getString(R.string.job), job)

                val jobDialog = JobDialog()
                jobDialog.arguments = bundle
                jobDialog.show(supportFragmentManager, null)
            } else {
                message?.let { toast(it) }
            }
        })

        viewModel.exception.observe(this, Observer {
            toast(it, Toast.LENGTH_LONG)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_map -> replaceFragment(MapFragment())
            R.id.nav_wallet -> replaceFragment(WalletFragment())
            R.id.nav_history -> replaceFragment(HistoryFragment())
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
                Intent(baseContext, ChangeProfileActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.option_change_password -> {
                Intent(baseContext, ChangePasswordActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.option_contact_admin -> contactAdmin()
            R.id.option_logout -> logout()
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
    }

    // When location is not enabled, the application will end.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isLocationProviderEnabled = Settings.Secure.isLocationProviderEnabled(
            baseContext.contentResolver,
            LocationManager.GPS_PROVIDER
        )
        if (!isLocationProviderEnabled && requestCode == REQUEST_CODE_LOCATION) {
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
                startActivityForResult(this, REQUEST_CODE_LOCATION)
            }
        }
    }

}
