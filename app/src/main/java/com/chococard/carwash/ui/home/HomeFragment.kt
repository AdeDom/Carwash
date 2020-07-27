package com.chococard.carwash.ui.home

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.util.extension.snackbar
import com.chococard.carwash.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    val viewModel by viewModel<HomeViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // call api
        progress_bar.show()
        viewModel.callHomeScore()

        // observe
        viewModel.getDbUser.observe { user ->
            if (user == null) return@observe
            val (_, fullName, _, _, _, image) = user
            tv_full_name.text = fullName
            iv_photo.setImageCircle(image)
        }

        viewModel.callSwitchSystem.observe { response ->
            progress_bar.hide()
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        }

        viewModel.getHomeScore.observe { response ->
            progress_bar.hide()
            val (success, message, homeScore) = response
            if (success) {
                tv_ratings.text = homeScore?.ratings
                tv_acceptance.text = homeScore?.acceptance
                tv_cancellation.text = homeScore?.cancellation
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.switchFlag.observe {
            if (it == FlagConstant.SWITCH_OFF) {
                iv_switch_off.show()
                iv_switch_on.hide()
            } else {
                iv_switch_off.hide()
                iv_switch_on.show()
            }
        }

        viewModel.getError.observe {
            progress_bar.hide()
            dialogError(it)
        }

        // set event
        iv_switch_frame.setOnClickListener { switchSystem() }
    }

    // TODO: 22/07/2563 observe switch real time
    override fun onResume() {
        super.onResume()
        // set widget switch button
        viewModel.initializeSwitchButton()
    }

    private fun switchSystem() {
        progress_bar.show()
        viewModel.callSwitchSystem()
    }

}
