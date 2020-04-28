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
import com.chococard.carwash.data.networks.NetworkHeaderInterceptor

abstract class BaseFragment<VM : ViewModel, F : ViewModelProvider.NewInstanceFactory>(
    private val layout: Int
) : Fragment() {

    protected lateinit var viewModel: VM
    protected lateinit var headerInterceptor: NetworkHeaderInterceptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //TODO create repository to base

        headerInterceptor = NetworkHeaderInterceptor(requireContext())

        viewModel = ViewModelProvider(this, factory()).get(viewModel())
    }

    abstract fun viewModel(): Class<VM>

    abstract fun factory(): F

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
