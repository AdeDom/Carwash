package com.chococard.carwash.util

import android.Manifest
import android.content.pm.PackageManager

object CommonsConstant {

    const val GRANTED = PackageManager.PERMISSION_GRANTED
    const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    const val REQUEST_CODE_IMAGE = 1
    const val REQUEST_CODE_LOCATION = 2
    const val REQUEST_CODE_PERMISSION = 3
    const val REQUEST_CODE_VERIFY_PHONE = 4
    const val REQUEST_CODE_DATE_RANGE = 5
    const val REQUEST_CODE_IMAGE_FRONT_BEFORE = 11
    const val REQUEST_CODE_IMAGE_BACK_BEFORE = 12
    const val REQUEST_CODE_IMAGE_LEFT_BEFORE = 13
    const val REQUEST_CODE_IMAGE_RIGHT_BEFORE = 14
    const val REQUEST_CODE_IMAGE_FRONT_AFTER = 15
    const val REQUEST_CODE_IMAGE_BACK_AFTER = 16
    const val REQUEST_CODE_IMAGE_LEFT_AFTER = 17
    const val REQUEST_CODE_IMAGE_RIGHT_AFTER = 18
    const val REQUEST_CODE_IMAGE_OTHER_IMAGE = 19

    const val MAX_COUNT_TIME = 15

    const val PREF_FILE = "pref_file"
    const val TOKEN = "token"
    const val REFRESH_TOKEN = "refresh_token"
    const val JOB = "job"
    const val LOGS_KEYS = "logs_keys"
    const val SWITCH = "switch"
    const val PHONE = "phone"
    const val DATE_RANGE_PICKER = "date_range_picker"
    const val HISTORY = "history"
    const val IMAGE = "image"

}
