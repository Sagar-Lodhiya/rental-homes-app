package com.rentalhomes.ui.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import java.lang.ref.WeakReference

open class BaseViewModel<N>(application: Application?) : AndroidViewModel(application!!) {
    private lateinit var mNavigator: WeakReference<N>

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    val navigator: N
        get() = mNavigator.get()!!

    fun setNavigator(mNavigator: N) {
        this.mNavigator = WeakReference(mNavigator)
    }

    fun attachContext(context: Context) {
        this.context = context
    }


}