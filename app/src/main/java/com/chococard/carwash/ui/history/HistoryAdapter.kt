package com.chococard.carwash.ui.history

import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.setImageCircle
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : BaseRecyclerView<History>() {

    override fun getLayout() = R.layout.item_history

    override fun onBindViewHolder(view: View, entity: History) {
        val (_, fullName, image, _, _, _, _, _, _, _, _, dateTime) = entity

        view.tv_full_name.text = fullName

        view.tv_date_time.text = dateTime

        image?.let { view.iv_photo.setImageCircle(it) }
    }

}
