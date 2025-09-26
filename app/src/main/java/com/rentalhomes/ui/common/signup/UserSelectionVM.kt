package com.rentalhomes.ui.common.signup

import android.app.Application
import com.rentalhomes.ui.base.BaseViewModel

class UserSelectionVM(application: Application) : BaseViewModel<UserSelectionNavigator>(application) {


    fun onSelectionClick(selection: Int){
        navigator.onSelectionClick(selection)
    }
    fun onCreateAccount(){
        navigator.onCreateAccount()
    }
}