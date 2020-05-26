package com.chococard.carwash.ui.wallet

import android.content.Intent
import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.addwallet.AddWalletActivity
import com.chococard.carwash.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_wallet.*

class WalletFragment : BaseFragment(R.layout.fragment_wallet) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun init() {
        // set event
        fab.setOnClickListener {
            Intent(context, AddWalletActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

}
