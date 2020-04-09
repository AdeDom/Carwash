package com.chococard.carwash.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel>(
    private val layout: () -> Int
) : Fragment() {

    lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout.invoke(), container, false)

}
