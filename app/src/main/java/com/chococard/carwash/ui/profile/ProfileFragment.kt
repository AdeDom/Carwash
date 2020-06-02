package com.chococard.carwash.ui.profile

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseFragment
import com.chococard.carwash.util.extension.setImageCircle
import com.chococard.carwash.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    val viewModel: ProfileViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getDbUser.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) return@Observer
            val (_, fullName, idCardNumber, phone, _, image) = user
            tv_full_name.text = fullName
            tv_identity_card.text = idCardNumber
            tv_phone.text = phone
            iv_photo.setImageCircle(image)
        })
    }

}
