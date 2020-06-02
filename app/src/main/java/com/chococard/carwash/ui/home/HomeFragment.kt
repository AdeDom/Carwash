package com.chococard.carwash.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.SwitchSystem
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

        viewModel.getDbUser.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) return@Observer
            val (_, fullName, _, _, _, image) = user
            tv_full_name.text = fullName
            iv_photo.setImageCircle(image)
        })

        viewModel.callSwitchSystem.observe(viewLifecycleOwner, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                switchButton()
            } else {
                message?.let { context.toast(it, Toast.LENGTH_LONG) }
            }
        })

        iv_switch_frame.setOnClickListener { switchSystem() }
    }

    private fun switchButton() {
        val switch = context?.readPref(CommonsConstant.SWITCH)
        if (switch == FlagConstant.SWITCH_OFF.toString()) {
            iv_switch_off.visibility = View.VISIBLE
            iv_switch_on.visibility = View.INVISIBLE
        } else {
            iv_switch_off.visibility = View.INVISIBLE
            iv_switch_on.visibility = View.VISIBLE
        }
    }

    private fun switchSystem() {
        progress_bar.show()
        val switch = context?.readPref(CommonsConstant.SWITCH)
        if (switch == FlagConstant.SWITCH_OFF.toString()) {
            context?.writePref(CommonsConstant.SWITCH, FlagConstant.SWITCH_ON.toString())
            viewModel.callSwitchSystem(SwitchSystem(FlagConstant.SWITCH_ON))
        } else {
            context?.writePref(CommonsConstant.SWITCH, FlagConstant.SWITCH_OFF.toString())
            viewModel.callSwitchSystem(SwitchSystem(FlagConstant.SWITCH_OFF))
        }
    }

}
