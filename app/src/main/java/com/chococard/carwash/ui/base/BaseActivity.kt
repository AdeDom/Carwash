package com.chococard.carwash.ui.base

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.writePref

abstract class BaseActivity<VM : ViewModel, F : ViewModelProvider.NewInstanceFactory> :
    AppCompatActivity() {

    protected lateinit var viewModel: VM
    protected lateinit var interceptor: NetworkConnectionInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interceptor = NetworkConnectionInterceptor(baseContext)

        viewModel = ViewModelProvider(this, factory()).get(viewModel())
    }

    abstract fun viewModel(): Class<VM>

    abstract fun factory(): F

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    fun setToolbar(toolbar: Toolbar) {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.overflowIcon?.setColorFilter(
            resources.getColor(android.R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    fun contactAdmin() = AlertDialog.Builder(this).apply {
        setTitle(R.string.contact_admin)
        setMessage(R.string.contact_system_administrator)
        setNegativeButton(android.R.string.cancel) { dialog, which ->
            dialog.dismiss()
        }
        setPositiveButton(android.R.string.ok) { dialog, which ->
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse(getString(R.string.contact_admin_tel))
                startActivity(this)
            }
        }
        setCancelable(false)
        show()
    }

    fun logout() = AlertDialog.Builder(this).apply {
        setTitle(R.string.logout)
        setMessage(R.string.do_you_really_want_to_log_out)
        setPositiveButton(android.R.string.cancel) { dialog, which ->
            dialog.dismiss()
        }
        setNegativeButton(android.R.string.ok) { dialog, which ->
            writePref(CommonsConstant.TOKEN, "")
            writePref(CommonsConstant.USER, "")
            Intent(baseContext, SignInActivity::class.java).apply {
                finishAffinity()
                startActivity(this)
            }
        }
        setCancelable(false)
        show()
    }

}
