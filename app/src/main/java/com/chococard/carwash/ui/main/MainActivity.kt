package com.chococard.carwash.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.UserApi
import com.chococard.carwash.data.repositories.UserRepository
import com.chococard.carwash.ui.change.ChangePasswordActivity
import com.chococard.carwash.ui.change.ChangeProfileActivity
import com.chococard.carwash.ui.main.history.HistoryFragment
import com.chococard.carwash.ui.main.map.MapFragment
import com.chococard.carwash.ui.main.wallet.WalletFragment
import com.chococard.carwash.util.BaseActivity
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.setToolbar
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainFactory(UserRepository(UserApi.invoke(networkConnectionInterceptor)))
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setToolbar(toolbar)

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(MapFragment())

        progress_bar.show()
        viewModel.fetchUser()

        viewModel.user.observe(this, Observer { response ->
            progress_bar.hide()
            response.message?.let { toast(it) }
            if (response.success) mUser = response.user
        })

        viewModel.exception = {
            progress_bar.hide()
            toast(it)
        }
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
                    putExtra(getString(R.string.user), mUser)
                    startActivity(this)
                }
            }
            R.id.option_change_password -> {
                Intent(baseContext, ChangePasswordActivity::class.java).apply {
                    putExtra(getString(R.string.user), mUser)
                    startActivity(this)
                }
            }
            R.id.option_contact_admin -> contactAdmin()
            R.id.option_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

}
