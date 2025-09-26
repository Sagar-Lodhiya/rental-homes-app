package com.rentalhomes.ui.common.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rentalhomes.utils.Constant.SPLASH_SCREEN_DELAY

class SplashVM : ViewModel() {
    private var isTimeOver: MutableLiveData<Boolean> = MutableLiveData()

    fun delaySplash(): MutableLiveData<Boolean> {
        Handler(Looper.getMainLooper()).postDelayed({ isTimeOver.postValue(true) }, SPLASH_SCREEN_DELAY)
        return isTimeOver
    }

}