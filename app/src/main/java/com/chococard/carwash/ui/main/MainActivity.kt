package com.chococard.carwash.ui.main

import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.JobAnswerRequest
import com.chococard.carwash.data.networks.request.LogsActiveRequest
import com.chococard.carwash.data.networks.request.SetLocationRequest
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
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseLocationActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(HomeFragment())

        bt_has_job.setOnClickListener {
            viewModel.callJobQuestion()
        }

        val listener: FlagJobListener = object : FlagJobListener {
            override fun onFlag(flag: Int) {
                progress_bar.show()
                viewModel.callJobAnswer(JobAnswerRequest(flag))
            }
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) viewModel.callFetchUserInfo()

        //call api
        val logsKeys = UUID.randomUUID().toString().replace("-", "")
        writePref(CommonsConstant.LOGS_KEYS, logsKeys)
        viewModel.callLogsActive(LogsActiveRequest(logsKeys, FlagConstant.LOGS_STATUS_ACTIVE))

        //observe
        viewModel.getUserInfo.observe(this, Observer { response ->
            val (success, message, user) = response
            if (success) {
                if (currentUser == null) {
                    startActivity<VPSignInActivity> { intent ->
                        intent.putExtra(CommonsConstant.PHONE, user?.phone)
                    }
                }
            } else {
                finishAffinity()
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getLogsActive.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getJobQuestion.observe(this, Observer { request ->
            val (success, message, jobQuestion) = request
            if (success) {
                val bundle = Bundle()
                bundle.putParcelable(CommonsConstant.JOB, jobQuestion)

                val jobDialog = JobDialog(listener)
                jobDialog.arguments = bundle
                jobDialog.show(supportFragmentManager, null)
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
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
            val (success, message) = response
            if (success) {
                writePref(CommonsConstant.TOKEN, "")
                writePref(CommonsConstant.REFRESH_TOKEN, "")
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getLocation.observe(this, Observer { response ->
            val (success, message) = response
            if (!success) {
                toast(message, Toast.LENGTH_LONG)
            }
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
            R.id.option_change_profile -> {
                startActivity<ChangeProfileActivity>()
            }
            R.id.option_change_password -> {
                startActivity<ChangePasswordActivity>()
            }
            R.id.option_contact_admin -> startActivityActionDial()
            R.id.option_logout -> dialogLogout {
                FirebaseAuth.getInstance().signOut()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        // set user logs active
        val logsKeys = readPref(CommonsConstant.LOGS_KEYS)
        viewModel.callLogsActive(LogsActiveRequest(logsKeys, FlagConstant.LOGS_STATUS_INACTIVE))
    }

    override fun onLocationChanged(location: Location?) {
        val setLocation = SetLocationRequest(location?.latitude, location?.longitude)
        viewModel.callSetLocation(setLocation)
    }

}
