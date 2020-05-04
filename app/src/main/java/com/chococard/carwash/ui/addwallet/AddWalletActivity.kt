package com.chococard.carwash.ui.addwallet

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.factory.AddWalletFactory
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.toast
import com.chococard.carwash.viewmodel.AddWalletViewModel
import kotlinx.android.synthetic.main.activity_add_wallet.*

class AddWalletActivity : BaseActivity<AddWalletViewModel, AddWalletFactory>() {

    override fun viewModel() = AddWalletViewModel::class.java

    override fun factory() = AddWalletFactory(repositoryHeader)

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
            tv_full_name.text = user?.fullName
        })

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_upload_image.setOnClickListener { selectImage() }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { addWallet() }
    }

    private fun selectImage() = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(this, CommonsConstant.REQUEST_CODE_IMAGE)
    }

    private fun addWallet() {
        toast("Add wallet")
        finish()
    }

}
