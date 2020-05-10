package com.chococard.carwash.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.data.networks.NetworkHeaderInterceptor
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.viewmodel.BaseViewModelFactory

abstract class BaseHeaderActivity<VM : ViewModel> : BaseActivity<VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val headerInterceptor = NetworkHeaderInterceptor(baseContext)
        val api = AppService.invoke(headerInterceptor)
        val db = AppDatabase(baseContext)
        val repository = BaseRepository(api, db)
        val factory = BaseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(viewModel())
    }

}
