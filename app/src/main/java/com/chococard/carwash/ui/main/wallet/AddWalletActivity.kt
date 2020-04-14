package com.chococard.carwash.ui.main.wallet

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.activity_add_wallet.*

class AddWalletActivity : BaseActivity<WalletViewModel, WalletFactory>() {

    override fun viewModel() = WalletViewModel::class.java

    override fun factory() = WalletFactory(MainRepository(MainApi.invoke(baseContext)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wallet)

        init()
    }

    private fun init() {
        // set widgets
        val user = MainActivity.sUser
        tv_full_name.text = user?.fullName

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_upload_image.setOnClickListener { selectImage() }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { addWallet() }

    }

    private fun addWallet() {
        toast("Add wallet")
        finish()
    }

}
