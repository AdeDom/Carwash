package com.chococard.carwash.ui.main

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.UserApi
import com.chococard.carwash.data.repositories.UserRepository
import com.chococard.carwash.ui.main.history.HistoryFragment
import com.chococard.carwash.ui.main.map.MapFragment
import com.chococard.carwash.ui.main.wallet.WalletFragment
import com.chococard.carwash.util.BaseActivity
import com.chococard.carwash.util.extension.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    val TAG = "MyTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainFactory(UserRepository(UserApi.invoke(networkConnectionInterceptor)))
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.overflowIcon?.setColorFilter(
            resources.getColor(android.R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) replaceFragment(MapFragment())

        viewModel.fetchUser()

        viewModel.user.observe(this, Observer {
            Log.d(TAG, ">>$it")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> toast("option_change_profile")
            R.id.option_change_password -> toast("option_change_password")
            R.id.option_contact_admin -> toast("option_contact_admin")
            R.id.option_logout -> toast("option_logout")
        }
        return super.onOptionsItemSelected(item)
    }

}
