package com.chococard.carwash.ui.addwallet

import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.selectImage
import com.chococard.carwash.util.extension.snackbar
import com.chococard.carwash.viewmodel.AddWalletViewModel
import kotlinx.android.synthetic.main.activity_add_wallet.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWalletActivity : BaseActivity() {

    val viewModel: AddWalletViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_wallet)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        // set widgets
        viewModel.getDbUser.observe(this, Observer { user ->
            if (user == null) return@Observer
            tv_full_name.text = user.fullName
        })

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_upload_image.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { addWallet() }
    }

    private fun addWallet() {
        root_layout.snackbar("Add wallet")
        finish()
    }

}
