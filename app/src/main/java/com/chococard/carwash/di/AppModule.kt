package com.chococard.carwash.di

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor
import com.chococard.carwash.data.networks.NetworkHeaderInterceptor
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { NetworkConnectionInterceptor(get()) }
    single { ConnectionAppService.invoke(get()) }
    single { ConnectionRepository(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }

    single { NetworkHeaderInterceptor(get()) }
    single { HeaderAppService.invoke(get()) }
    single { AppDatabase(get()) }
    single { HeaderRepository(get(), get()) }
    viewModel { AddWalletViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { ChangeProfileViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { PaymentViewModel(get()) }

}
