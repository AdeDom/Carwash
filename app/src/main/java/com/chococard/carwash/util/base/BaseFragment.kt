package com.chococard.carwash.util.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor

abstract class BaseFragment<VM : BaseViewModel>(
    private val layout: Int
) : Fragment() {

    lateinit var viewModel: VM
    lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        networkConnectionInterceptor = NetworkConnectionInterceptor(requireContext())
    }

}
