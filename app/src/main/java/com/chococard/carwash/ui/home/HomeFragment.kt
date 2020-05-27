package com.chococard.carwash.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.SwitchFlag
import com.chococard.carwash.util.extension.readSwitch
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val switch = context?.readSwitch()
        if (switch == SwitchFlag.SWITCH_OFF.toString()) {
            iv_switch_off.visibility = View.VISIBLE
            iv_switch_on.visibility = View.INVISIBLE
        } else {
            iv_switch_off.visibility = View.INVISIBLE
            iv_switch_on.visibility = View.VISIBLE
        }

        viewModel.getDbUser.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) return@Observer
            val (_, fullName, _, _, _, image) = user
            tv_full_name.text = fullName
            image?.let { iv_photo.setImageCircle(it) }
        })
    }

}
