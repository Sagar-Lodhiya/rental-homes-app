package com.rentalhomes.ui.buyer.createtemplate

interface CreateTemplateNavigator {
    //Add Item click from adapter
    fun onAddItemClicked(position: Int, categoryId: Int)

    //Radio button click
    fun onSetAsDefaultClicked()

    fun onAfterDeletedItem(position: Int)

    fun showProgress()

    fun hideProgress()

    fun setMessageComingFromServer(message: String?)

    fun addTemplatesToCategory(obj: Any)

    fun onCustomTemplateSuccess()
}