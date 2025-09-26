package com.rentalhomes.data.network

interface ResponseCallback {
    fun onSuccess(`object`: Any?, name: String?)
    fun onFail(`object`: Any?)
}