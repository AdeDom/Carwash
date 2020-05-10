package com.chococard.carwash.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.viewmodel.BaseViewModelFactory

abstract class BaseNormalActivity<VM : ViewModel> : BaseActivity<VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val connectionInterceptor = NetworkConnectionInterceptor(baseContext)
        val api = AppService.invoke(connectionInterceptor)
        val db = AppDatabase(baseContext)
        val repository = BaseRepository(api, db)
        val factory = BaseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(viewModel())
    }

}
