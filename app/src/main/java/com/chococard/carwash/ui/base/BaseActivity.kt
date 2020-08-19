package com.chococard.carwash.ui.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.TokenExpiredException
import com.chococard.carwash.util.extension.hasPermission
import com.chococard.carwash.util.extension.startActivity

abstract class BaseActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            recreate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    protected inline fun <reified T> LiveData<T>.observe(crossinline onNext: (T) -> Unit) {
        observe(this@BaseActivity, Observer { onNext(it) })
    }

    protected fun LiveData<Throwable>.observeError() {
        observe(this@BaseActivity, Observer {
            it.printStackTrace()
            dialogError(it.message)

            if (it is TokenExpiredException) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            }
        })
    }

    protected fun setToolbar(toolbar: Toolbar) {
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    protected fun dialogContactAdmin(
        @StringRes title: Int = R.string.contact_admin,
        @StringRes message: Int = R.string.contact_admin_message,
        contactAdmin: () -> Unit
    ) = AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { _, _ ->
            contactAdmin.invoke()
        }
        setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

    protected fun dialogLogout(
        @StringRes title: Int = R.string.logout,
        @StringRes message: Int = R.string.do_you_really_want_to_log_out,
        logout: () -> Unit
    ) = AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        setNegativeButton(android.R.string.ok) { _, _ ->
            logout.invoke()
        }
        setCancelable(false)
        show()
    }

    protected fun dialogError(message: String?) = AlertDialog.Builder(this).apply {
        setTitle(R.string.error)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

}
