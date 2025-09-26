package com.rentalhomes.ui.buyer.profile

interface BuyerProfileNavigator {
    fun onEditProfileClick()
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
}