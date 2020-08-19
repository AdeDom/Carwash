package com.chococard.carwash.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.chococard.carwash.R
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.networks.request.LogsActiveRequest
import com.chococard.carwash.data.networks.request.SetLocationRequest
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.ui.base.BaseLocationActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.history.HistoryFragment
import com.chococard.carwash.ui.home.HomeFragment
import com.chococard.carwash.ui.navigation.NavigationActivity
import com.chococard.carwash.ui.profile.ProfileFragment
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.ui.verifyotp.OtpSignInActivity
import com.chococard.carwash.ui.wallet.WalletFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseLocationActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    JobDialog.FlagJobListener {

    val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(HomeFragment())

        //observe
        viewModel.attachFirstTime.observe {
            if (savedInstanceState == null) {
                viewModel.callLogsActive(LogsActiveRequest(FlagConstant.LOGS_STATUS_ACTIVE))
                viewModel.initSignalR()
            }
        }

        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.getDbUserInfoLiveData.observe { userInfo ->
            if (userInfo == null) return@observe

            // fetch user info & sign in firebase
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity<OtpSignInActivity> { intent ->
                    intent.putExtra(CommonsConstant.PHONE, userInfo.phone)
                }
            }
        }

        viewModel.getLogsActive.observe { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        }

        viewModel.getJobAnswer.observe { response ->
            val (success, _, _) = response
            if (success) {
                startActivity<NavigationActivity> {
                    finishAffinity()
                }
            }
        }

        viewModel.getLogout.observe { response ->
            val (success, message) = response
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.getLocation.observe { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        }

        viewModel.getJobQuestion.observe { response ->
            Log.d(TAG, "onCreate: $response")
            val (success, message, job) = response
            if (success) onHasJobAlert(job) else root_layout.snackbar(message)
        }

        viewModel.callSwitchSystem.observe { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        }

        viewModel.counterExit.observe {
            if (it > 1) finishAffinity()
        }

        viewModel.error.observeError()
    }

    private fun onHasJobAlert(job: Job?) {
        // Notification
        val builder = NotificationCompat.Builder(
            baseContext,
            CommonsConstant.CAR_WASH_CHANNEL
        ).apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setSubText(job?.location)
            setContentTitle(job?.fullName)
            setContentText(job?.packageName)
            priority = NotificationCompat.PRIORITY_HIGH
        }.build()

        val currentTime = System.currentTimeMillis().toInt()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CommonsConstant.CAR_WASH_CHANNEL,
                CommonsConstant.CAR_WASH_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
            }
            manager.createNotificationChannel(channel)
            NotificationManagerCompat.from(baseContext).notify(currentTime, builder)
        } else {
            manager.notify(currentTime, builder)
        }

        // dialog
        val bundle = Bundle()
        bundle.putParcelable(CommonsConstant.JOB, job)
        val jobDialog = JobDialog(this)

        jobDialog.arguments = bundle
        jobDialog.show(supportFragmentManager, null)
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
            R.id.option_change_profile -> startActivity<ChangeProfileActivity>()
            R.id.option_change_password -> startActivity<ChangePasswordActivity>()
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout { viewModel.callLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        // switch
        viewModel.callSwitchSystem(SwitchSystemRequest(FlagConstant.SWITCH_OFF))

        // set user logs active
        viewModel.callLogsActive(LogsActiveRequest(FlagConstant.LOGS_STATUS_INACTIVE))
    }

    override fun onLocationResult(location: Location) {
        super.onLocationResult(location)
        val setLocation = SetLocationRequest(location.latitude, location.longitude)
        viewModel.callSetLocation(setLocation)
    }

    override fun onFlag(flag: Int) = viewModel.callJobAnswer(flag)

    override fun onBackPressed() {
        toast(getString(R.string.finish_affinity))
        viewModel.setCounterExit()
    }

    companion object {
        private const val TAG = "SignalR"
    }

}
