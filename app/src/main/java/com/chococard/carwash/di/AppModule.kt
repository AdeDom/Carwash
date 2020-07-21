package com.chococard.carwash.di

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor
import com.chococard.carwash.data.networks.NetworkHeaderInterceptor
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.data.sharedpreference.SharedPreferenceImpl
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.repositories.ConnectionRepositoryImpl
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.repositories.HeaderRepositoryImpl
import com.chococard.carwash.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // db
    single { AppDatabase(get()) }

    // Shared Preference
    single<SharedPreference> { SharedPreferenceImpl(get()) }

    //connection
    single { NetworkConnectionInterceptor(get()) }
    single { ConnectionAppService.invoke(get()) }
    single<ConnectionRepository> { ConnectionRepositoryImpl(get(), get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { RequestOtpViewModel(get()) }
    viewModel { SplashScreenViewModel(get(), get()) }

    //header connection
    single { NetworkHeaderInterceptor(get(), get()) }
    single { HeaderAppService.invoke(get()) }
    single<HeaderRepository> { HeaderRepositoryImpl(get(), get(), get()) }
    viewModel { AddWalletViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { ChangeProfileViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { PaymentViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { NavigationViewModel(get()) }
    viewModel { ReportViewModel(get()) }
    viewModel { ServiceViewModel(get()) }
    viewModel { ServiceInfoViewModel(get()) }
    viewModel { HistoryDetailViewModel(get()) }
    viewModel { ViewImageViewModel(get()) }

}
