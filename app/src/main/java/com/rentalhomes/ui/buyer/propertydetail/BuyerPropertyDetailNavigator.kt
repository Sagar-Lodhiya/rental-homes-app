package com.rentalhomes.ui.buyer.propertydetail

interface BuyerPropertyDetailNavigator {
    //Back button click
    fun onBackPress()

    //Tier tabs clicks
    fun onPropertyDetailClicked()

    fun onQuestionClicked()

    fun onTier1Clicked()

    fun onTier2Clicked()

    fun onTier3Clicked()

    //Like DisLike features
    fun updateProgressBar()

    fun onCompareClick()

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(message: String?)

    fun onQRClick()
}