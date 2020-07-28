package com.chococard.carwash.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chococard.carwash.R

abstract class BaseFragment(@LayoutRes private val layout: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layout, container, false)

    protected inline fun <reified T> LiveData<T>.observe(crossinline onNext: (T) -> Unit) {
        observe(this@BaseFragment, Observer { onNext(it) })
    }

    protected fun LiveData<Throwable>.observeError() {
        observe(this@BaseFragment, Observer {
            it?.printStackTrace()
            dialogError(it?.message)
        })
    }

    protected fun dialogError(message: String?) = context?.let {
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
