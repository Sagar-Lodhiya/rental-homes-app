package com.rentalhomes.ui.common.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rentalhomes.R
import com.rentalhomes.RentalHomesApp
import com.rentalhomes.data.pref.AppSessionManager
import com.rentalhomes.ui.agent.homescreen.AgentHomeActivity
import com.rentalhomes.ui.buyer.home.BuyerHomeActivity
import com.rentalhomes.ui.common.signin.SignInActivity
import com.rentalhomes.utils.GlobalMethods

class SplashActivity : AppCompatActivity() {
    private lateinit var viewModel: SplashVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observerTimer()
    }

    private fun init() {
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(SplashVM::class.java)
    }

    private fun observerTimer() {
        viewModel.delaySplash().observe(this, {
            if (it) {
                gotoScreen()
            }
        })
    }

    private fun gotoScreen() {
        if (RentalHomesApp.appSessionManager.getSession(this)) {
            when {
                RentalHomesApp.appSessionManager.getInt(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE
                ) == 1 -> {
                    intent = Intent(this@SplashActivity, AgentHomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                RentalHomesApp.appSessionManager.getInt(
                    this,
                    AppSessionManager.PREF_NAME,
                    AppSessionManager.PREF_KEY_USER_TYPE
                ) == 2 -> {
                    intent = Intent(this@SplashActivity, BuyerHomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else -> {
                    intent = Intent(this@SplashActivity, SignInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        } else {
            intent = Intent(this@SplashActivity, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}