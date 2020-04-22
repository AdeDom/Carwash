package com.chococard.carwash.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<VM : ViewModel, F : ViewModelProvider.NewInstanceFactory>(
    private val layout: Int
) : Fragment() {

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory()).get(viewModel())
    }

    abstract fun viewModel(): Class<VM>

    abstract fun factory(): F

}
