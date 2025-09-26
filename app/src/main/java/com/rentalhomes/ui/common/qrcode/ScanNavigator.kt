package com.rentalhomes.ui.common.qrcode

interface ScanNavigator {
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onAddPropertyComplete()
}