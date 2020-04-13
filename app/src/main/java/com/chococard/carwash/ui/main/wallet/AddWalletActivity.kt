package com.chococard.carwash.ui.main.wallet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.MainApi
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.activity_add_wallet.*

class AddWalletActivity : BaseActivity<WalletViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wallet)

        init()
    }

    private fun init() {
        val user = intent.getParcelableExtra<User>(getString(R.string.user))

        val factory = WalletFactory(MainRepository(MainApi.invoke(baseContext)))
        viewModel = ViewModelProvider(this, factory).get(WalletViewModel::class.java)

        // set widgets
        tv_full_name.text = user.fullName

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
