package com.chococard.carwash.ui.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switchButton()

        // call api
        progress_bar.show()
        viewModel.callHomeScore()

        // observe
        viewModel.getDbUser.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) return@Observer
            val (_, fullName, _, _, _, image) = user
            tv_full_name.text = fullName
            iv_photo.setImageCircle(image)
        })

        viewModel.callSwitchSystem.observe(viewLifecycleOwner, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) switchButton() else root_layout.snackbar(message)
        })

        viewModel.getHomeScore.observe(viewLifecycleOwner, Observer { response ->
            progress_bar.hide()
            val (success, message, homeScore) = response
            if (success) {
                tv_ratings.text = homeScore?.ratings
                tv_acceptance.text = homeScore?.acceptance
                tv_cancellation.text = homeScore?.cancellation
            } else {
                root_layout.snackbar(message)
            }
        })

        viewModel.getError.observe(viewLifecycleOwner, Observer {
            progress_bar.hide()
            dialogError(it)
        })

        // set event
        iv_switch_frame.setOnClickListener { switchSystem() }
    }

    private fun switchButton() {
        val switch = context?.readPref(CommonsConstant.SWITCH)
        if (switch == FlagConstant.SWITCH_OFF.toString()) {
            iv_switch_off.show()
            iv_switch_on.hide()
        } else {
            iv_switch_off.hide()
            iv_switch_on.show()
        }
    }

    // TODO: 11/07/2563 change code to flag and observe button switch
    private fun switchSystem() {
        progress_bar.show()
        val switch = context?.readPref(CommonsConstant.SWITCH)
        if (switch == FlagConstant.SWITCH_OFF.toString()) {
            context?.writePref(CommonsConstant.SWITCH, FlagConstant.SWITCH_ON.toString())
            viewModel.callSwitchSystem(SwitchSystemRequest(FlagConstant.SWITCH_ON))
        } else {
            context?.writePref(CommonsConstant.SWITCH, FlagConstant.SWITCH_OFF.toString())
            viewModel.callSwitchSystem(SwitchSystemRequest(FlagConstant.SWITCH_OFF))
        }
    }

}
