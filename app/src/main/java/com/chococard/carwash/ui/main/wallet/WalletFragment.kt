package com.chococard.carwash.ui.main.wallet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.util.BaseFragment

class WalletFragment : BaseFragment<WalletViewModel>({ R.layout.fragment_wallet }) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
    }

}
