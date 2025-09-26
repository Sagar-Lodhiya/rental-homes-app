package com.rentalhomes.ui.buyer.editprofile

interface BuyerEditProfileNavigator {
    fun onSaveClicked()
    fun onUpdateClicked()
    fun onRemoveClicked()
    fun isValid(): Boolean
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onEditProfileComplete(message: String?)
}