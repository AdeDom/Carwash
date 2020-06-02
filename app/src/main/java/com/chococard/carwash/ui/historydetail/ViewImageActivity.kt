package com.chococard.carwash.ui.historydetail

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.setImageFromInternet
import kotlinx.android.synthetic.main.activity_view_image.*

class ViewImageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        val image = intent.getStringExtra(CommonsConstant.IMAGE)
        iv_image.setImageFromInternet(image)

        iv_arrow_back.setOnClickListener { onBackPressed() }
    }

}
