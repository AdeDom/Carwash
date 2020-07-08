package com.chococard.carwash.ui.historydetail

import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.data.models.ImageService
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.setImageFromInternet
import kotlinx.android.synthetic.main.item_image_history_detail.view.*

class HistoryDetailAdapter : BaseRecyclerView<ImageService>() {

    override fun getLayout(): Int = R.layout.item_image_history_detail

    override fun View.onBindViewHolder(data: ImageService) {
        iv_image_service.setImageFromInternet(data.image)
    }

}
