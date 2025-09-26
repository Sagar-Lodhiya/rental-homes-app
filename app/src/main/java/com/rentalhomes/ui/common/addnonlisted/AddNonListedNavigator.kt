package com.rentalhomes.ui.common.addnonlisted

interface AddNonListedNavigator {
    fun onSaveClicked()
    fun onGalleryClicked()
    fun onCameraClicked()
    fun onPropertyPhotoClicked()
    fun showProgress()
    fun hideProgress()
    fun setMessageComingFromServer(it: String)

    //Counter clicks
    fun onMinusBedClicked()
    fun onPlusBedClicked()
    fun onMinusBathClicked()
    fun onPlusBathClicked()
    fun onMinusCarClicked()
    fun onPlusCarClicked()
}