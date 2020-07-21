package com.chococard.carwash.ui.main

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.JobAnswerRequest
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
import com.chococard.carwash.ui.verifyphone.VPSignInActivity
import com.chococard.carwash.ui.wallet.WalletFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseLocationActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    JobDialog.FlagJobListener {

    val viewModel: MainViewModel by viewModel()
    private var counterExit = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(HomeFragment())

        //call api
        viewModel.callLogsActive(LogsActiveRequest(FlagConstant.LOGS_STATUS_ACTIVE))

        //observe
        viewModel.getDbUser.observe(this, Observer { user ->
            // fetch user info & sign in firebase
            when {
                user == null -> viewModel.callFetchUserInfo()
                FirebaseAuth.getInstance().currentUser == null -> {
                    viewModel.initSignalR(user.userId)
                    startActivity<VPSignInActivity> { intent ->
                        intent.putExtra(CommonsConstant.PHONE, user.phone)
                    }
                }
                else -> viewModel.initSignalR(user.userId)
            }
        })

        viewModel.getUserInfo.observe(this, Observer { response ->
            val (success, message, _) = response
            if (!success) {
                finishAffinity()
                root_layout.snackbar(message)
            }
        })

        viewModel.getLogsActive.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        })

        viewModel.getJobAnswer.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, _, _) = response
            if (success) {
                startActivity<NavigationActivity> {
                    finishAffinity()
                }
            }
        })

        viewModel.getLogout.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        })

        viewModel.getLocation.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        })

        viewModel.getJobQuestion.observe(this, Observer { response ->
            Log.d(TAG, "onCreate: $response")
            val (success, message, job) = response
            if (success) {
                val bundle = Bundle()
                bundle.putParcelable(CommonsConstant.JOB, job)
                val jobDialog = JobDialog(this)

                jobDialog.arguments = bundle
                jobDialog.show(supportFragmentManager, null)
            } else {
                root_layout.snackbar(message)
            }
        })

        viewModel.callSwitchSystem.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
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
            R.id.option_change_profile -> startActivity<ChangeProfileActivity>()
            R.id.option_change_password -> startActivity<ChangePasswordActivity>()
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout {
                progress_bar.show()
                viewModel.callLogout()
            }
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

    override fun onLocationChanged(location: Location?) {
        val setLocation = SetLocationRequest(location?.latitude, location?.longitude)
        viewModel.callSetLocation(setLocation)
    }

    override fun onFlag(answer: JobAnswerRequest) {
        progress_bar.show()
        viewModel.callJobAnswer(answer)
    }

    override fun onBackPressed() {
        Coroutines.main {
            toast(getString(R.string.finish_affinity))
            if (counterExit > 0) finishAffinity()
            counterExit++
            delay(2_000)
            counterExit = 0
        }
    }

    companion object {
        private const val TAG = "SignalR"
    }

}
