package com.rentalhomes.ui.common.signup

interface UserSelectionNavigator {
    fun showProgress()

    fun hideProgress()

    fun onCreateAccount()

    fun onSelectionClick(selection: Int)
}