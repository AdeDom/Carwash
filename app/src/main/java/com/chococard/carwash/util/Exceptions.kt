package com.chococard.carwash.util

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
class TokenExpiredException(message: String) : IOException(message)
