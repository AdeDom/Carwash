package com.chococard.carwash.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.chococard.carwash.R

abstract class BaseFragment(private val layout: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    fun dialogError(message: String) = context?.let {
        AlertDialog.Builder(it).apply {
            setTitle(R.string.error)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

}
