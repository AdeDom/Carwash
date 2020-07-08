package com.chococard.carwash.ui.service

import android.view.View
import com.chococard.carwash.R
import com.chococard.carwash.data.models.ImageService
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.setImageFromInternet
import kotlinx.android.synthetic.main.item_other_image_service.view.*

class ServiceAdapter : BaseRecyclerView<ImageService>() {

    var onRemoveOtherImage: ((ImageService) -> Unit)? = null

    override fun getLayout() = R.layout.item_other_image_service

    override fun View.onBindViewHolder(data: ImageService) {
        iv_other_image.setImageFromInternet(data.image)

        card_remove_other_image.setOnClickListener {
            onRemoveOtherImage?.invoke(data)
        }
    }

}
