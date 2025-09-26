package com.rentalhomes.ui.agent.editprofile

interface EditProfileNavigator {
    fun onSaveClicked()
    fun onUpdateClicked()
    fun onRemoveClicked()
    fun isValid(): Boolean
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)
    fun onEditProfileComplete(message: String?)
}