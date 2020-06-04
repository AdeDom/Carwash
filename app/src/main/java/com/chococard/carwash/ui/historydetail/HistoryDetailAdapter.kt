package com.chococard.carwash.ui.historydetail

import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.data.models.OtherImage
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.setImageFromInternet
import kotlinx.android.synthetic.main.item_other_image.view.*

class HistoryDetailAdapter : BaseRecyclerView<OtherImage>() {

    override fun getLayout(): Int = R.layout.item_other_image

    override fun onBindViewHolder(view: View, entity: OtherImage) {
        view.iv_other_image.setImageFromInternet(entity.image)
    }

}
