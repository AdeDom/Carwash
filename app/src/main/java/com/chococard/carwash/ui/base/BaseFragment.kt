package com.chococard.carwash.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.data.networks.NetworkHeaderInterceptor
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.viewmodel.BaseViewModelFactory

abstract class BaseFragment<VM : ViewModel>(private val layout: Int) : Fragment() {

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val headerInterceptor = NetworkHeaderInterceptor(requireContext())
        val api = AppService.invoke(headerInterceptor)
        val db = AppDatabase(requireContext())
        val repository = BaseRepository(api, db)
        val factory = BaseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(viewModel())
    }

    abstract fun viewModel(): Class<VM>

    fun dialogError(message: String) = context?.let {
        AlertDialog.Builder(it).apply {
            setTitle(R.string.error)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

}
