package com.rentalhomes.utils

import android.content.Context
import android.content.Intent
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.ui.common.signin.SignInActivity

object NavigationUtil {
    fun goToSignIn(context: Context){
        RentalHomesApp.appSessionManager.setSession(context, false)
        RentalHomesApp.appSessionManager.clearPreference(context)
        var intent = Intent(context, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}