package com.chococard.carwash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.repositories.BaseRepository

@Suppress("UNCHECKED_CAST")
class BaseViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddWalletViewModel::class.java) -> AddWalletViewModel(repository) as T
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository) as T
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> ChangePasswordViewModel(repository) as T
            modelClass.isAssignableFrom(ChangeProfileViewModel::class.java) -> ChangeProfileViewModel(repository) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(repository) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            modelClass.isAssignableFrom(MapViewModel::class.java) -> MapViewModel(repository) as T
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> PaymentViewModel(repository) as T
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> SignInViewModel(repository) as T
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> SignUpViewModel(repository) as T
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> SplashScreenViewModel(repository) as T
            modelClass.isAssignableFrom(WalletViewModel::class.java) -> WalletViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel class not found...")
        }
    }
}
