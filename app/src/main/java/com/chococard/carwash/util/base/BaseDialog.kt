package com.chococard.carwash.util.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.chococard.carwash.ui.main.OnAttachListener

abstract class BaseDialog(private val layout: Int) : DialogFragment() {

    protected lateinit var listener: OnAttachListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnAttachListener
    }

}
