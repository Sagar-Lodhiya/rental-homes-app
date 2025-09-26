package com.rentalhomes.ui.agent.buyerFeedback

interface BuyerFeedbackNavigator {

    fun onFeedbackClicked()

    fun onTier1Clicked()

    fun onTier2Clicked()

    fun onTier3Clicked()

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(message: String?)

}