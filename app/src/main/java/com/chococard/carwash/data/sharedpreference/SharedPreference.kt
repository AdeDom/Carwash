package com.chococard.carwash.data.sharedpreference

interface SharedPreference {
    var accessToken: String
    var refreshToken: String
    var jobId: Int
    var switchFlag: Int
}
