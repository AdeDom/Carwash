package com.chococard.carwash.ui.splashscreen

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.auth.AuthActivity
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.navigation.NavigationActivity
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.viewmodel.RootNavigation
import com.chococard.carwash.viewmodel.SplashScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : BaseActivity() {

    val viewModel by viewModel<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel.rootNavigation.observe {
            when (it) {
                RootNavigation.HAS_JOB -> startActivity<NavigationActivity> { finish() }
                RootNavigation.MAIN -> startActivity<MainActivity> { finish() }
                RootNavigation.AUTHENTICATION -> startActivity<AuthActivity> { finish() }
            }
        }

        viewModel.attachFirstTime.observe {
            viewModel.initialize()
        }
    }

}
