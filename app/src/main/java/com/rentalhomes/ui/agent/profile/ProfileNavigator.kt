package com.rentalhomes.ui.agent.profile

interface ProfileNavigator {
    fun onEditProfileClicked()

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(it: String)
}