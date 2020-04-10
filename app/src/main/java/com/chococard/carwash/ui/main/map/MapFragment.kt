package com.chococard.carwash.ui.main.map

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.util.BaseFragment

class MapFragment : BaseFragment<MapViewModel>({ R.layout.fragment_map }) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
    }

}
