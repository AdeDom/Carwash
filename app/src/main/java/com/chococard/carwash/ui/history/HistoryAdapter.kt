package com.chococard.carwash.ui.history

import com.chococard.carwash.R
import com.chococard.carwash.data.models.History
import com.chococard.carwash.ui.BaseRecyclerView
import com.chococard.carwash.util.extension.getLocality
import com.chococard.carwash.util.extension.loadCircle
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : BaseRecyclerView<History>(
    R.layout.item_history,
    { view, history ->
        val (_, fullName, image, _, _, service, price, latitude, longitude, _, vehicleRegistration, dateTime) = history

        view.tv_full_name.text = fullName

        view.tv_service.text = service

        if (latitude != null && longitude != null)
            view.tv_location.text = view.context.getLocality(latitude, longitude)

        view.tv_vehicle_registration.text = vehicleRegistration

        view.tv_price.text = price

        view.tv_date_time.text = dateTime

        image?.let { view.iv_photo.loadCircle(it) }
    }
)
