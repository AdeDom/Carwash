package com.chococard.carwash.ui.home

import android.os.Bundle
import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.hide
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.util.extension.show
import com.chococard.carwash.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set widget
        viewModel.getDbUserLiveData.observe {
            tv_full_name.text = it.fullName
            iv_photo.setImageCircle(it.image)
        }

        // observe
        viewModel.state.observe { state ->
            // progress bar
            if (state.loading) progress_bar.show() else progress_bar.hide()

            // switch system
            if (state.switchSystem == FlagConstant.SWITCH_OFF) {
                iv_switch_off.show()
                iv_switch_on.hide()
            } else {
                iv_switch_off.hide()
                iv_switch_on.show()
            }

            // home score
            val (success, _, homeScore) = state.homeScore
            if (success) {
                tv_ratings.text = homeScore?.ratings
                tv_acceptance.text = homeScore?.acceptance
                tv_cancellation.text = homeScore?.cancellation
            }
        }

        viewModel.error.observeError()

        // set event
        iv_switch_frame.setOnClickListener { viewModel.callSwitchSystem() }
    }

    override fun onResume() {
        super.onResume()
        // fetch switch system
        viewModel.callHomeScore()
    }

}
