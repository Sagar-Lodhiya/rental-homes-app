package com.rentalhomes.ui.common.signin

interface SignInNavigator {
    fun showProgress()

    fun hideProgress()

    fun isValid(): Boolean

    fun goToAgent()

    fun goToBuyer()

    fun setMessageComingFromServer(it: String)

    fun onSignInCompleted()
}
