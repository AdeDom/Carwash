package com.chococard.carwash.util.extension

import android.content.Context
import android.widget.Toast
import com.chococard.carwash.R

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.writePref(key: Int, values: String) =
    getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
        .putString(getString(key), values)
        .apply()

fun Context.readPref(key: Int) =
    getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        .getString(getString(key), "") ?: ""
