package com.chococard.carwash.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chococard.carwash.util.CommonsConstant

class SharedPreferenceImpl(context: Context) : SharedPreference {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(CommonsConstant.PREF_FILE, Context.MODE_PRIVATE)

    override var accessToken: String
        get() = sharedPreference.getString(CommonsConstant.ACCESS_TOKEN, "").orEmpty()
        set(value) {
            sharedPreference.edit {
                putString(CommonsConstant.ACCESS_TOKEN, value)
            }
        }

    override var refreshToken: String
        get() = sharedPreference.getString(CommonsConstant.REFRESH_TOKEN, "").orEmpty()
        set(value) {
            sharedPreference.edit {
                putString(CommonsConstant.REFRESH_TOKEN, value)
            }
        }

    override var switchFlag: Int
        get() = sharedPreference.getInt(CommonsConstant.SWITCH, 0)
        set(value) {
            sharedPreference.edit {
                putInt(CommonsConstant.SWITCH, value)
            }
        }

}
