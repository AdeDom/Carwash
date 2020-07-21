package com.chococard.carwash.ui.history

import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.setImageCircle
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : BaseRecyclerView<History>() {

    override fun getLayout() = R.layout.item_history

    override fun View.onBindViewHolder(data: History) {
        val (_, fullName, imageProfile, _, _, _, _, jobDateTime) = data

        tv_full_name.text = fullName

        tv_date_time.text = jobDateTime

        iv_photo.setImageCircle(imageProfile)
    }

}
